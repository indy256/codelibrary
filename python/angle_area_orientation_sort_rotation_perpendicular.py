class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __lt__(self, other):
        return self.x < other.x

    def __str__(self):
        return "(" + str(self.x) + "," + str(self.y) + ")"


def test():
    points = [Point(2, 1), Point(1, 1)]
    points.sort()
    for p in points:
        print(p)


test()
