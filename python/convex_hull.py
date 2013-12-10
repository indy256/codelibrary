def convex_hull(points):
    points = sorted(set(points))

    if len(points) <= 1:
        return points

    def cross(a, b, c):
        return (b[0] - a[0]) * (c[1] - a[1]) - (b[1] - a[1]) * (c[0] - a[0])

    lower = []
    for p in points:
        while len(lower) >= 2 and cross(lower[-2], lower[-1], p) >= 0:
            lower.pop()
        lower.append(p)

    upper = []
    for p in reversed(points):
        while len(upper) >= 2 and cross(upper[-2], upper[-1], p) >= 0:
            upper.pop()
        upper.append(p)

    return lower[:-1] + upper[:-1]

# tests
import random

assert convex_hull([(0, 0), (0, 0)]) == [(0, 0)]
assert convex_hull([(i // 10, i % 10) for i in range(100)]) == [(0, 0), (0, 9), (9, 9), (9, 0)]
print(len(convex_hull([(random.randint(0, 10000), random.randint(0, 10000)) for i in range(10000)])))
