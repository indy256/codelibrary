def convex_hull(points):
    points = sorted(set(points))

    if len(points) <= 1:
        return points

    def hull(points):
        def cross(a, b, c):
            return (b[0] - a[0]) * (c[1] - a[1]) - (b[1] - a[1]) * (c[0] - a[0])

        res = []
        for p in points:
            while len(res) >= 2 and cross(res[-2], res[-1], p) >= 0:
                res.pop()
            res.append(p)
        return res

    return hull(points)[:-1] + hull(reversed(points))[:-1]


def test():
    import random

    assert convex_hull([(0, 0), (0, 0)]) == [(0, 0)]
    assert convex_hull([(i // 10, i % 10) for i in range(100)]) == [(0, 0), (0, 9), (9, 9), (9, 0)]
    print(len(convex_hull([(random.randint(0, 10000), random.randint(0, 10000)) for i in range(10000)])))


test()
