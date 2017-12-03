function knightFastestStep(k1, k2) {

    //  size of square board
    var N = 8;
    var knightPos = [k1.charCodeAt(0) - 96, Number(k1[1])];
    var targetPos = [k2.charCodeAt(0) - 96, Number(k2[1])];
 
    return minSteps(knightPos, targetPos, N);
}

function Cell(x, y, dis)
{
    this.x = x;
    this.y = y;
    this.dis = dis;
};
 
//  Utility method returns true if (x, y) lies inside Board
isInside = (x, y, N) => x >= 1 && x <= N && y >= 1 && y <= N
 
//  Method returns minimum step to reach target position
function minSteps(k1, k2, N)
{
    //  x and y direction, where a knight can move
    dx = [-2, -1, 1, 2, -2, -1, 1, 2];
    dy = [-1, -2, -2, -1, 1, 2, 2, 1];
 
    //  queue for storing states of knight in board
    var q = [];
 
    //  push starting position of knight with 0 distance
    q.push(new Cell(k1[0], k1[1], 0));
 
    var visit = new Array(N + 1);
 
    //  make all cell unvisited
    for (i = 1; i <= N; i++) {
        a = new Array(N + 1);
        for (j = 1; j <= N; j++)
            a[j] = false;
        visit[i] = a;
    }
 console.log(visit)
    console.log(k1[0] + " " + k1[1])
    //  visit starting state
    visit[k1[0]][k1[1]] = true;
 
    //  loop untill we have one element in queue
    while (q.length > 0)
    {
        t = q.shift();
        visit[t.x][t.y] = true;
 
        // if current cell is equal to target cell,
        // return its distance
        if (t.x == k2[0] && t.y == k2[1])
            return t.dis;
 
 
        //  loop for all reahable states
        for (i = 0; i < 8; i++)
        {
            x = t.x + dx[i];
            y = t.y + dy[i];
 
            // If rechable state is not yet visited and
            // inside board, push that state into queue
            if (isInside(x, y, N) && !visit[x][y])
                q.push(new Cell(x, y, t.dis + 1));
 
        }
    }
}
 
