import ControlState
import pygame
from pygame.joystick import Joystick
import math
import ControllerUtils
import Drone
import Consts
import os
# import NetworkUtilscontrolState


class ControlLogic:
    # Initialize Pygame
    pygame.init()

    consts = Consts.Consts()

    def __init__(self, drone: Drone, speed=0.1, state: ControlState = ControlState.State.KEYBOARD) -> None:
        self.state = state
        self.drone: Drone = drone
        self.speed = speed

        self.updateUser: bool = True
        self.stateLog: bool = None

        self.pressedPower: bool = False

        # Speed increase/decrease rate (adjust these for slower speed changes)
        self.acceleration_rate = 0.001
        self.deceleration_rate = 0.001

        self.forward_speed = 0.0
        self.lateral_speed = 0.0

        # Maximum speed limit
        self.max_speed = 0.5

        self.controllerUtils = ControllerUtils.ControllerUtils()
        # self.networkUtils = NetworkUtils.NetworkUtils()

        # left and right wall
        self.xWall = 4.6

        # Crash state and speed where the drone will count as crashed
        self.has_crashed = False

        # Initialize wind properties
        self.wind_direction = 0  # Angle in radians
        self.wind_strength = 0.01  # Speed at which wind affects the drone

        self.controller: Joystick = None
        self.controllerCount: int = -1

        if state == ControlState.State.CONTROLLER:
            self.controller = self.controllerUtils.getController()
            self.controllerCount = self.controllerUtils.CONTROLLER_COUNT

    def update(self):
        # Mode checker
        self.mode_selector()

        if self.state == ControlState.State.KEYBOARD:
            self.__use_keyboard()
        elif self.state == ControlState.State.CONTROLLER:
            self.__use_controller()
        elif self.state == ControlState.State.PACKET:
            self.__use_packet()

    def mode_selector(self):
        isLog = os.path.isfile("./Log/log.txt")
        if isLog != self.stateLog:
            self.stateLog = isLog
            self.updateUser = True

        if isLog:
            self.state = ControlState.State.PACKET
            if self.updateUser:
                print(ControlState.StateColor.C1 + "State: " +
                      ControlState.StateColor.C2 + "PACKET")
                self.updateUser = False

        else:
            self.state = ControlState.State.KEYBOARD
            if self.updateUser:
                print(ControlState.StateColor.C1 + "State: " +
                      ControlState.StateColor.C2 + "KEYBOARD")
                self.updateUser = False

    def __mode_checker_old(self):
        keys = pygame.key.get_pressed()
        for event in pygame.event.get():
            if keys[pygame.K_1]:
                self.state = ControlState.State.KEYBOARD
                break
            elif keys[pygame.K_2]:
                self.state = ControlState.State.PACKET
                break

    def __use_keyboard(self):
        keys = pygame.key.get_pressed()
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

            if keys[pygame.K_SPACE] and not self.pressedPower:
                pressedPower = True
                self.drone.TogglePower()
            if not keys[pygame.K_SPACE] and self.pressedPower:
                pressedPower = False

        if self.drone.power:
            # Forward/Backward Movement
            if keys[pygame.K_s]:
                if self.forward_speed >= 0:  # Moving forward or stationary
                    self.forward_speed = min(
                        self.forward_speed + self.acceleration_rate, self.max_speed)
                    self.drone.orientation[0] = math.radians(
                        10)  # Pitch adjustment
                else:  # Moving backward, so decelerate first
                    self.forward_speed = min(
                        self.forward_speed + self.deceleration_rate, 0)
                    self.drone.orientation[0] = 0  # Resetting pitch
            elif keys[pygame.K_w]:
                if self.forward_speed <= 0:  # Moving backward or stationary
                    self.forward_speed = max(
                        self.forward_speed - self.acceleration_rate, -self.max_speed)
                    # Pitch adjustment
                    self.drone.orientation[0] = math.radians(-10)
                else:  # Moving forward, so decelerate first
                    self.forward_speed = max(
                        self.forward_speed - self.deceleration_rate, 0)
                    self.drone.orientation[0] = 0  # Resetting pitch
            else:
                # Slow down if no forward/backward keys are pressed
                if self.forward_speed > 0:
                    self.forward_speed = max(
                        self.forward_speed - self.deceleration_rate, 0)
                elif self.forward_speed < 0:
                    self.forward_speed = min(
                        self.forward_speed + self.deceleration_rate, 0)
                # Resetting pitch when not moving
                self.drone.orientation[0] = 0

            if keys[pygame.K_r]:
                # Rotate wind direction
                self.wind_direction += math.radians(1.9)
            if keys[pygame.K_t]:
                # Increase wind strength
                self.wind_strength = max(
                    self.wind_strength, self.wind_strength + 0.0005)
            if keys[pygame.K_y]:
                # Decrease wind strength
                self.wind_strength = max(0, self.wind_strength - 0.0005)

            self.drone.position[0] += self.wind_strength * \
                math.cos(self.wind_direction)
            self.drone.position[2] += self.wind_strength * \
                math.sin(self.wind_direction)

            # Lateral Movement (Left/Right)
            if keys[pygame.K_d]:
                self.lateral_speed = min(
                    self.lateral_speed + self.acceleration_rate, self.max_speed)
                self.drone.orientation[2] = math.radians(10)
            elif keys[pygame.K_a]:
                self.lateral_speed = max(
                    self.lateral_speed - self.acceleration_rate, -self.max_speed)
                self.drone.orientation[2] = math.radians(-10)
            else:
                # Slow down if no lateral keys are pressed
                if self.lateral_speed > 0:
                    self.lateral_speed = max(
                        self.lateral_speed - self.deceleration_rate, 0)
                elif self.lateral_speed < 0:
                    self.lateral_speed = min(
                        self.lateral_speed + self.deceleration_rate, 0)
                self.drone.orientation[2] = 0

            # Apply movement
            dx = -math.sin(self.drone.orientation[1]) * self.forward_speed
            dz = -math.cos(self.drone.orientation[1]) * self.forward_speed
            self.drone.position[0] += self.lateral_speed  # Lateral movement
            self.drone.position[0] += dx  # Forward/Backward movement
            self.drone.position[2] += dz

            if keys[pygame.K_q]:
                self.drone.position[1] += self.speed
            elif keys[pygame.K_e]:
                self.drone.position[1] -= self.speed

    def __use_keyboard_old(self):
        keys = pygame.key.get_pressed()
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        # Calculate movement direction based on drone orientation
        dx = -math.sin(self.drone.orientation[1]) * self.speed
        dz = -math.cos(self.drone.orientation[1]) * self.speed

        # Move the drone based on key inputs and adjust orientation
        if keys[pygame.K_SPACE] and self.pressedPower == False:
            self.pressedPower = True
            self.drone.TogglePower()
        if keys[pygame.K_SPACE] == False and self.pressedPower == True:
            self.pressedPower = False

        if self.drone.power:
            if keys[pygame.K_a]:
                self.drone.position[0] += dz
                self.drone.position[2] += dx
                self.drone.orientation[2] = math.radians(-10)
            elif keys[pygame.K_d]:
                self.drone.position[0] -= dz
                self.drone.position[2] -= dx
                self.drone.orientation[2] = math.radians(10)
            else:
                self.drone.orientation[2] = 0

            if keys[pygame.K_s]:
                self.drone.position[0] += dx
                self.drone.position[2] += dz
                self.drone.orientation[0] = math.radians(10)
            elif keys[pygame.K_w]:
                self.drone.position[0] -= dx
                self.drone.position[2] -= dz
                self.drone.orientation[0] = math.radians(-10)
            else:
                self.drone.orientation[0] = 0

            if keys[pygame.K_q]:
                self.drone.position[1] += self.speed
            elif keys[pygame.K_e]:
                self.drone.position[1] -= self.speed

    def __use_controller(self):
        if pygame.joystick.get_count() < self.controllerCount:
            print("Disconnection detected")
            # TODO - fix this bug of reconnection ctr att error
            # controller = controllerUtils.getController()
            running = False
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
        # Calculate movement direction based on drone orientation
        dx = -math.sin(self.drone.orientation[1]) * self.speed
        dz = -math.cos(self.drone.orientation[1]) * self.speed

        if event.type == pygame.JOYBUTTONDOWN:
            btn = event.__getattribute__('button')
            if btn == self.controllerUtils.MAP.btnMapping["BTN1"] and not self.pressedPower:
                self.pressedPower = True
                self.drone.TogglePower()
        if event.type == pygame.JOYBUTTONUP:
            btn = event.__getattribute__('button')
            if btn == self.controllerUtils.MAP.btnMapping["BTN1"] and self.pressedPower:
                self.pressedPower = False

        if self.drone.power:
            x = self.controller.get_axis(0)
            y = self.controller.get_axis(1)
            z = self.controller.get_axis(3)
            if abs(x) > self.consts.DRIFT_VAL:
                if x < 0:
                    self.drone.position[0] += dz
                    self.drone.position[2] += dx
                    self.drone.orientation[2] = math.radians(-10)
                else:
                    self.drone.position[0] -= dz
                    self.drone.position[2] -= dx
                    self.drone.orientation[2] = math.radians(10)
            else:
                self.drone.orientation[2] = 0

            if abs(y) > self.consts.DRIFT_VAL:
                if y > 0:
                    self.drone.position[0] += dx
                    self.drone.position[2] += dz
                    self.drone.orientation[0] = math.radians(10)
                else:
                    self.drone.position[0] -= dx
                    self.drone.position[2] -= dz
                    self.drone.orientation[0] = math.radians(-10)
            else:
                self.drone.orientation[0] = 0

            if abs(z) > self.consts.DRIFT_VAL:
                if z > 0:
                    self.drone.position[1] += self.speed
                else:
                    self.drone.position[1] -= self.speed

    def __use_packet(self):
        packet = self.__get_packet_from_log()
        packet = packet.split('_')

        # error check
        if len(packet) != 9 or packet[1] != "Drone":
            print("Issue with the packet! Not of the expected format")
            return

        # power
        if packet[1] != "Drone":
            self.drone.power = True

        # Motors
        for i in range(2, 6):
            self.drone.motors[i-2] = packet[i]

        # LEDs
        LED = [0, 0, 0]
        for i in range(6, 9):
            LED[i-6] = packet[i]
        self.drone.LED = tuple(LED)

        self.drone.power = True
        self.drone.UpdatePosition()

    def __get_packet_from_log(self) -> str:
        recent_packet = ""
        try:
            with open('./Log/log.txt', 'r') as log:
                cmds = log.read().splitlines()
                recent_packet = cmds[-1]
                log.close()
        except:
            pass
        return recent_packet
