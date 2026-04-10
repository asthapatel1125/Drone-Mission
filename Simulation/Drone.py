from enum import Enum


class Drone:
    def __init__(self, name: str = "Drone1", starting_position=[0, 0, 0]) -> None:

        self.name: str = name

        self.power: bool = False

        self.LED = (0, 0, 0)

        self.motors = [0, 0, 0, 0]

        self.position = [0, 0, 0]  # Drone position and orientation
        self.orientation = [0, 0, 0]  # [pitch, yaw, roll]

    def TogglePower(self) -> None:
        self.power = not self.power

    def UpdatePosition(self, speed: float = 0.01):
        # TODO -- readd this check
        # if not self.power:
        #    return

        # if self.motors[0] == self.motors[1] == self.motors[2] == self.motors[3]:
        #    return
        """
        //      Motor layout
        //     [0]        [1]
        //         X   X
        //           X
        //         X   X
        //     [3]       [2]
       """

        F: int = 0  # forward
        B: int = 0  # backward
        L: int = 0  # left
        R: int = 0  # right
        U: int = 0  # up
        D: int = 0  # down

        antiG: int = 130

        # 2 ==> major movement in X direction
        # 1 ==> minor movement in X direction

        if self.PWMmag(self.motors[0]) < self.PWMmag(self.motors[3]):
            F = +2
            L = +1
        if self.PWMmag(self.motors[1]) < self.PWMmag(self.motors[2]):
            F = +2
            R = +1

        if self.PWMmag(self.motors[0]) > self.PWMmag(self.motors[3]):
            B = +2
            R = +1
        if self.PWMmag(self.motors[1]) > self.PWMmag(self.motors[2]):
            B = +2
            L = +1

        if self.PWMmag(self.motors[0]) < self.PWMmag(self.motors[1]):
            L = +2
            F = +1
        if self.PWMmag(self.motors[3]) < self.PWMmag(self.motors[2]):
            L = +2
            B = +1
        if self.PWMmag(self.motors[0]) > self.PWMmag(self.motors[1]):
            R = +2
            B = +1
        if self.PWMmag(self.motors[3]) > self.PWMmag(self.motors[2]):
            R = +2
            F = +1

        if self.PWMmag(self.motors[0]) > self.PWMmag(antiG):
            U += 0.25
        if self.PWMmag(self.motors[1]) > self.PWMmag(antiG):
            U += 0.25
        if self.PWMmag(self.motors[2]) > self.PWMmag(antiG):
            U += 0.25
        if self.PWMmag(self.motors[3]) > self.PWMmag(antiG):
            U += 0.25

        if self.PWMmag(self.motors[0]) < self.PWMmag(antiG):
            D += 0.25
        if self.PWMmag(self.motors[1]) < self.PWMmag(antiG):
            D += 0.25
        if self.PWMmag(self.motors[2]) < self.PWMmag(antiG):
            D += 0.25
        if self.PWMmag(self.motors[3]) < self.PWMmag(antiG):
            D += 0.25

        # movement logic
        self.position[0] += speed*(L-R)
        self.position[1] += speed*(D-U)
        self.position[2] += speed*(B-F)

    def PWMmag(self, pwm: int):
        if int(pwm) > 255 or int(pwm) < 0:
            return 127
        return abs(127 - int(pwm))

    def printPosition(self):
        print("Drone Position:")
        for i in self.position:
            print(i)


# @TODO - Add more functions
# @TODO - Add pygame clock and use that to limit movement