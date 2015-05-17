function convexHull(points) {
    points.sort(function (a, b) {
        return a.x != b.x ? a.x - b.x : a.y - b.y;
    });

    function removeMiddle(a, b, c) {
        var cross = (a.x - b.x) * (c.y - b.y) - (a.y - b.y) * (c.x - b.x);
        var dot = (a.x - b.x) * (c.x - b.x) + (a.y - b.y) * (c.y - b.y);
        return cross < 0 || cross == 0 && dot <= 0;
    }

    var n = points.length;
    var hull = [];

    for (var i = 0; i < 2 * n; i++) {
        var j = i < n ? i : 2 * n - 1 - i;
        while (hull.length >= 2 && removeMiddle(hull[hull.length - 2], hull[hull.length - 1], points[j]))
            hull.pop();
        hull.push(points[j]);
    }

    hull.pop();
    return hull;
}

// tests
console.log(convexHull([{x: 0, y: 0}]));
console.log(convexHull([{x: 0, y: 0}, {x: 0, y: 0}]));
console.log(convexHull([{x: 0, y: 0}, {x: 0, y: 1}, {x: 0, y: 2}, {x: 2, y: 2}, {x: 2, y: 1}, {x: 2, y: 0}, {x: 1, y: 1}]));
