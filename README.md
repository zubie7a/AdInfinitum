## Santiago Zubieta
#### 2012

## AdInfinitum

![][01]

This is an application of the [Cyclic Cellular Automaton](http://en.wikipedia.org/wiki/Cyclic_cellular_automaton) theory, which from an initial state of several random dispersed colors, and a set of rules _(such as colors converting a certain neighbors into their color)_, some linear patters start to emerge. This becomes even crazier if there's a _'random'_ element inserted into the chance of _'survival'_ of a color when being converted by another, breaking the linearity and creating more organically looking spirals.

This was made for educational purposes, and is licensed under the MIT License.

This uses uses **SFML 2.0** for **C++** and **AWT/Swing** for **Java**.

I installed **SFML** in **OSX** using [**brew**](http://brew.sh/). Get it. Its incredibly amazing and powerful.  
Just a `$ brew install sfml` will do it.

### The Process

A grid full of individual cells. Each with a color value chosen at random. Overall, there are 12 different colors that wrap around.

Suddenly, each cell becomes prey, its hunter, is the 'next' color in the rules:

`1 -> 2 / 2 -> 3 / n -> n+1 / 12 -> 1`

If everything goes according to the plan, a curious linear and cyclical pattern is formed.

If everything goes according to the plan, a curious linear and cyclical pattern is formed. But then, once in a while, rules change, and chaos appears. This chaos, is the prey fighting back. It doesn't mean the prey will eat the hunter, but it means that a prey may survive an iteration, 50/50 odds. The linear cyclical pattern is broken, the result is a more organic and natural looking spiral.

### C++ Version Video
[![][02]](http://www.youtube.com/watch?v=ggDWurIu6zI)

### Java Version Video
[![][03]](http://www.youtube.com/watch?v=84-AlIMW7Fk)

[01]: https://i.imgur.com/aQalSUZ.png "AdInfinitum"
[02]: https://i.imgur.com/TzWw3Tz.png "AdInfinitum"
[03]: https://i.imgur.com/Fi07Exw.png "AdInfinitum"