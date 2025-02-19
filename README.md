# Console-Tetris
using JNI & c++ 

# v5
- fixed drawGhost's delay - v5
  >_fixed canDrawGhost boolean_
  
- fixed destroyLine respawning destroyed blocks - v5
  > _after .destroyLine(), had to .shiftQueue() instantly._

- proper block queue algorithm (bag of 7) - v4
  >_added blockList class: handles who now and who next_
  
- fixed rotation collision - v3
  >_added logic to indent init drawing point_
  
- can press space to drop + a visible ghost guide (took me 6h) - v2
  >_embedded drawGhost logic inside drawBlock()_
  
- players can move down, right, left, rotate (z,x) - v1
- complete lines will be destroyed, score works - v1


## To-Do:
- fix double drawBlock call

## Future To-Do's
- start from game menu
- allow pause/resume (p)
- save & load game
- refactor logic & design patterns




## Time Log (total 32h)
_note: started at 2 Feb 2025. (approx 8 hours maybe) but started recording time at 10 Feb 2025_

**start**

> pushed at 14 Feb 10pm - (16.5 hours)

**v1**
> pushed at 16 Feb 4:32pm - (10.5 hours)

**v2**
> pushed at 16 Feb 6:53pm - (6 minutes)

**v3**
> pushed at 18 Feb 4:16pm - (3.25 hours)

**v4**
> pushed at 19 Feb 2:05pm - (1.7 hours)

 **v5**



## Github
1. git add .
2. git commit -m " message "
3. git pull origin master --rebase
4. git push origin master
