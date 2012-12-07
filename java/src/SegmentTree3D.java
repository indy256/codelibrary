public class SegmentTree3D {
  public static int max(int[][][] t, int x1, int y1, int z1, int x2, int y2, int z2) {
    int n = t.length >> 1; x1 += n; x2 += n;
    int m = t[0].length >> 1; y1 += m; y2 += m;
    int k = t[0][0].length >> 1; z1 += k; z2 += k;
    int res = Integer.MIN_VALUE;
    for(int lx=x1,rx=x2;lx<=rx;lx=(lx+1)>>1,rx=(rx-1)>>1)
      for(int ly=y1,ry=y2;ly<=ry;ly=(ly+1)>>1,ry=(ry-1)>>1)
        for(int lz=z1,rz=z2;lz<=rz;lz=(lz+1)>>1,rz=(rz-1)>>1){
          if((lx&1)!=0&&(ly&1)!=0&&(lz&1)!=0) res=Math.max(res,t[lx][ly][lz]);
          if((lx&1)!=0&&(ly&1)!=0&&(rz&1)==0) res=Math.max(res,t[lx][ly][rz]);
          if((lx&1)!=0&&(ry&1)==0&&(lz&1)!=0) res=Math.max(res,t[lx][ry][lz]);
          if((lx&1)!=0&&(ry&1)==0&&(rz&1)==0) res=Math.max(res,t[lx][ry][rz]);
          if((rx&1)==0&&(ly&1)!=0&&(lz&1)!=0) res=Math.max(res,t[rx][ly][lz]);
          if((rx&1)==0&&(ly&1)!=0&&(rz&1)==0) res=Math.max(res,t[rx][ly][rz]);
          if((rx&1)==0&&(ry&1)==0&&(lz&1)!=0) res=Math.max(res,t[rx][ry][lz]);
          if((rx&1)==0&&(ry&1)==0&&(rz&1)==0) res=Math.max(res,t[rx][ry][rz]);
        }
    return res;
  }

  public static void add(int[][][] t, int x, int y, int z, int value) {
    x += t.length >> 1; y += t[0].length >> 1; z += t[0][0].length >> 1;
    t[x][y][z] += value;
    for (int tx = x; tx > 0; tx >>= 1)
      for (int ty = y; ty > 0; ty >>= 1)
        for (int tz = z; tz > 0; tz >>= 1)
          t[tx][ty][tz] = Math.max(t[tx][ty][tz], t[x][y][z]);
  }

  public static void main(String[] args) {
    int[][][] t = new int[20][40][60];
    add(t, 0, 0, 1, 1);
    add(t, 3, 4, 5, 2);
    System.out.println(2 == max(t, 0, 0, 0, 9, 19, 29));
  }
}
