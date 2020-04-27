## configuration

un fichier ".cshrc" doit être créer dans le home.
ce fichier contient des triplets de configurations et doit être de la forme

```
setenv RELEASE_DIR /path/to/release/file
setenv CLASSPATH .
```
note : le path de release_dir doit être relatif au home!!

ensuite, le fichier javaConfig.tcsh contient

```
#!/bin/tcsh

setenv CLASSPATH "$HOME"/"$RELEASE_DIR"/path/to/source/folder

exit(0)
```

pour compiler le programme : `tcsh makeDoit`
cela va compiler le programme et créer les .class ->
en fait, cela appelle le makefile, qui lui contient la commande "javac"

pour executer le programme : `tcsh startDoit`

note : concirérant que le shebang est mis, il est possible d'utilisé directement ./makeDoit et ./startDoit

le fichier "makeDoit" contient le path vers le fichier rdf à lire.

### options
le fichier `options.dat` a plusieurs options soit :

-	fragThreshold : le nombre minimum de nœud par fragment
-	cloneThreshold : la distance maximum après laquel deux fragments ne sont plus considéré comme des clones
-	algoDistance : le nom de l’algorithme de calcul de distance, entre « manhatan », « manhatanNormal » et « euclidien »
-	algoDataVec : s’il faut utiliser les vecteur de donnée (true) ou les types de nœuds (false) pour calculer les distances des fragments.

# Licence

Ce projet est sous liscence MIT

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    The Software is provided “as is”, without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders X be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the Software."


