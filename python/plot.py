from cmath import sin

import matplotlib.pyplot as plt

plt.plot([sin(x) for x in range(100)])
plt.show()
