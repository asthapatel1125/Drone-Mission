import pygame
from pygame.joystick import Joystick
import math
import ControllerUtils
import Drone
import Consts
import ControlLogic
import ControlState
import sys
import os

def run():
    # Initialize Pygame
    pygame.init()

    consts = Consts.Consts()

    controlState = ControlState.State.KEYBOARD 
    speed = 0.1
    drone = Drone.Drone()
    controlLogic = ControlLogic.ControlLogic(drone, speed, controlState)

    pressedPower: bool = False

    controllerUtils = ControllerUtils.ControllerUtils()
    #networkUtils = NetworkUtils.NetworkUtils()


    # Set up display
    width, height = 800, 600
    win = pygame.display.set_mode((width, height))
    pygame.display.set_caption('3D Drone Simulation')

    grey = (150, 150, 150)
    white = (255, 255, 255)
    red = (255, 0, 0)
    green = (0, 255, 0)
    droneColor = red

    # Load your ground image
    ground_sky_image_original = pygame.image.load("./Images/ground.png")
    ground_sky_image = pygame.transform.scale(ground_sky_image_original, (800, 120))

    # Load your sky image
    sky = pygame.image.load("./Images/sky.png")
    sky = pygame.transform.scale(sky, (800, 480))

    sky2 = pygame.image.load("./Images/sky2.png")
    sky2 = pygame.transform.scale(sky2, (800, 480))


    current_background = sky  # Start with the first sky image


    # Define drone as a cube
    drone_vertices = [
        [1.2, 0.4, 1.2],
        [1.2, 0.4, -1.2],
        [1.2, -0.2, 1.2],
        [1.2, -0.2, -1.2],
        [-1.2, 0.4, 1.2],
        [-1.2, 0.4, -1.2],
        [-1.2, -0.2, 1.2],
        [-1.2, -0.2, -1.2]
    ]

    # Define the faces of the drone
    drone_faces = [
        [0, 1, 5, 4],
        [2, 3, 7, 6],
        [0, 1, 3, 2],
        [1, 5, 7, 3],
        [5, 4, 6, 7],
        [4, 0, 2, 6]
    ]

    # Define propellers at the four corners of the top face of the drone
    propeller_vertices = [
        [1.4, -0.2, 1.4],
        [1.4, -0.2, -1.4],
        [-1.4, -0.2, 1.4],
        [-1.4, -0.2, -1.4]
    ]

    # Camera properties
    fov = 256
    camera_z = 5

    # Movement speed
    speed = 0.1

    # Initial angle for propeller rotation
    propeller_rotation_angle = 0

    # Length of the propeller blades
    propeller_length = 10

    xWall = 4.6
    forward_speed = 0.0
    lateral_speed = 0.0
    # Main loop
    running = True

    def calculate_right_wall_x(drone_z):
        return 1.62 * drone_z + 4.5

    def calculate_left_wall_x(drone_z):
        return -1.62 * drone_z - 4.5

    def calculate_ground_position():
        return 0.657*drone.position[2]+2.7

    while running:
        # the controlls :)
        controlLogic.update()


    # ----------------------------------------------------------------------------------------------
        if drone.position[2]<-2.5:
            drone.position[2]=-2.499
            
        right_wall_x = calculate_right_wall_x(drone.position[2])
        left_wall_x = calculate_left_wall_x(drone.position[2])

        if drone.position[0] > right_wall_x:
            drone.position[0] = left_wall_x + (drone.position[0] - right_wall_x)
            current_background = sky2 if current_background == sky else sky  # Toggle background
        elif drone.position[0] < left_wall_x:
            drone.position[0] = right_wall_x - (left_wall_x - drone.position[0])
            current_background = sky2 if current_background == sky else sky  # Toggle back

        #invisible wall infront the camera
        if drone.position[2] <-3:
            drone.position[2] = -2.9
            forward_speed = 0
        prev_drone_position = list(drone.position)
        
        if drone.position[1] >= calculate_ground_position():
            drone.position[1] = calculate_ground_position()
            has_crashed = True
        else:
            has_crashed= False

        # Handle crash
        if has_crashed and (forward_speed > 0.06 or lateral_speed > 0.06 or forward_speed < -0.06 or lateral_speed < -0.06):
            # Stop all movement
            forward_speed = 0
            lateral_speed = 0
            drone.power = False
            drone.position = [0,0,0]
            has_crashed=False
            
        
        # Update propeller rotation angle for the animation
        if drone.power:
            drone.LED = green
            propeller_rotation_angle += math.radians(10)
            if propeller_rotation_angle > 2 * math.pi:
                propeller_rotation_angle -= 2 * math.pi
        else:
            drone.LED = red

        # Blit the combined ground and sky image onto the screen
        win.blit(ground_sky_image, (0, 480))
        win.blit(current_background, (0,0))

        projected_points = []
        for vertex in drone_vertices:
            # Apply pitch rotation
            rotated_x = vertex[0]
            rotated_y = vertex[1] * math.cos(drone.orientation[0]) - vertex[2] * math.sin(drone.orientation[0])
            rotated_z = vertex[1] * math.sin(drone.orientation[0]) + vertex[2] * math.cos(drone.orientation[0])

            # Apply yaw rotation
            final_x = rotated_x * math.cos(drone.orientation[1]) - rotated_z * math.sin(drone.orientation[1])
            final_z = rotated_x * math.sin(drone.orientation[1]) + rotated_z * math.cos(drone.orientation[1])
            final_y = rotated_y

            # Apply roll rotation
            final_y = rotated_y * math.cos(drone.orientation[2]) + final_x * math.sin(drone.orientation[2])
            final_x = rotated_y * -math.sin(drone.orientation[2]) + final_x * math.cos(drone.orientation[2])

            # Perspective projection
            perspective = fov / (final_z + camera_z + drone.position[2])
            projected_x = (final_x + drone.position[0]) * perspective + width // 2
            projected_y = (final_y + drone.position[1]) * perspective + height // 2

            projected_points.append([int(projected_x), int(projected_y)])

        # Fill the faces of the drone
        for face in drone_faces:
            pygame.draw.polygon(win, drone.LED, [projected_points[j] for j in face])

        # Translations, rotations, and projections of propeller vertices to 2D space
        propeller_points = []
        for vertex in propeller_vertices:
            # Same rotation and projection calculations as used for drone vertices
            rotated_x = vertex[0]
            rotated_y = vertex[1] * math.cos(drone.orientation[0]) - vertex[2] * math.sin(drone.orientation[0])
            rotated_z = vertex[1] * math.sin(drone.orientation[0]) + vertex[2] * math.cos(drone.orientation[0])

            final_x = rotated_x * math.cos(drone.orientation[1]) - rotated_z * math.sin(drone.orientation[1])
            final_z = rotated_x * math.sin(drone.orientation[1]) + rotated_z * math.cos(drone.orientation[1])
            final_y = rotated_y

            final_y = rotated_y * math.cos(drone.orientation[2]) + final_x * math.sin(drone.orientation[2])
            final_x = rotated_y * -math.sin(drone.orientation[2]) + final_x * math.cos(drone.orientation[2])

            perspective = fov / (final_z + camera_z + drone.position[2])
            scaled_propeller_length = propeller_length * perspective
            projected_x = (final_x + drone.position[0]) * perspective + width // 2
            projected_y = (final_y + drone.position[1]) * perspective + height // 2

            propeller_points.append([int(projected_x), int(projected_y)])

        # Draw propeller blades as rotating lines, scaled with perspective
        for center in propeller_points:
            perspective = fov / (final_z + camera_z + drone.position[2])
            scaled_propeller_length = propeller_length * perspective * 0.03  
            x1 = center[0] + scaled_propeller_length * math.cos(propeller_rotation_angle)
            y1 = center[1] + scaled_propeller_length * math.sin(propeller_rotation_angle)
            x2 = center[0] + scaled_propeller_length * math.cos(propeller_rotation_angle + math.pi)
            y2 = center[1] + scaled_propeller_length * math.sin(propeller_rotation_angle + math.pi)

            pygame.draw.line(win, drone.LED, (int(x1), int(y1)), (int(x2), int(y2)), 3)

        pygame.display.flip()
        pygame.time.delay(10)

    pygame.quit()

if __name__ == "__main__":
    try:
        #os.remove("./Log/log.txt")
        pass
    except:
        pass 
    run()
