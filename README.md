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
