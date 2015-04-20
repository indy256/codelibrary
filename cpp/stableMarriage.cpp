/**
 *  /////////////////////////////
 *  // Stable Marriage Problem //
 *  /////////////////////////////
 *
 * MAIN FUNCTION: stableMarriage()
 *      Takes a set of m men and n women, where each person has
 *      an integer preference for each of the persons of the opposite
 *      sex. Produces a matching of each man to some woman. The matching
 *      will have the following properties:
 *          - Each man is assigned a different woman (n must be at least m).
 *          - No two couples M1W1 and M2W2 will be unstable.
 *      Two couples are unstable if
 *          - M1 prefers W2 over W1 and
 *          - W1 prefers M2 over M1.
 * INPUTS:
 *      - m:        number of men.
 *      - n:        number of women (must be at least as large as m).
 *      - L[i][]:   the list of women in order of decreasing preference of man i.
 *      - R[j][i]:  the attractiveness of i to j.
 * OUTPUTS:
 *      - L2R[]:    the mate of man i (always between 0 and n-1)
 *      - R2L[]:    the mate of woman j (or -1 if single)
 * ALGORITHM:
 *      The algorithm is greedy and runs in time O(m^2).
 * FIELD TESTING:
 *      - UVa 11119: Chemical Attraction
 *
 * LAST MODIFIED:
 *      October 14, 2006
 *
 * This file is part of my library of algorithms found here:
 *      http://shygypsy.com/tools/
 * LICENSE:
 *      http://shygypsy.com/tools/LICENSE.html
 * Copyright (c) 2006
 **/
#include <string.h>

#define MAXM 1024
#define MAXW 1024

int m, n;
int L[MAXM][MAXW], R[MAXW][MAXM];
int L2R[MAXM], R2L[MAXW];

int p[MAXM];

void stableMarriage()
{
    static int p[128];
    memset( R2L, -1, sizeof( R2L ) );
    memset( p, 0, sizeof( p ) );

    // Each man proposes...
    for( int i = 0; i < m; i++ )
    {
        int man = i;
        while( man >= 0 )
        {
            // to the next woman on his list in order of decreasing preference,
            // until one of them accepts;
            int wom;
            while( 1 )
            {
                wom = L[man][p[man]++];
                if( R2L[wom] < 0 || R[wom][man] > R[wom][R2L[wom]] ) break;
            }

            // Remember the old husband of wom.
            int hubby = R2L[wom];

            // Marry man and wom.
            R2L[L2R[man] = wom] = man;

            // If a guy was dumped in the process, remarry him now.
            man = hubby;
        }
    }
}
