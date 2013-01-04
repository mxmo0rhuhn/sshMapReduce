sshMapReduce
============

Projekt für die Fächer Methoden der Programmierung und Algorithmen und Datenstrukturen


Beispiel zur Erläuterung:

Für das Beispiel wollen wir ermitteln, wie viele Linux-Benutzer es pro Land gibt. Dazu wollen wir verschiedene Quellen beachten. (Um das Beispiel etwas einzuschränken verwenden wir nur ein paar wenige Länder. Ausserdem nehmen wir an, es wäre in jeder Quelle möglich, die Anzahl der Benutzer aus einem bestimmten Land zu ermitteln. z.B. Mittels einer Analyse der Foren-Mitglieder).


== INPUT für das MapReduce Framework ==
-> Für unser Beispiel geben wir dem MapReduce Framework drei Werte. Nebst der Map- und Reduce-Funktion geben wir ihm eine Liste von Inputs, welche dann elementweise als Input für eine Instanz einer Map-Funktion verwendet werden.

Map Funktion: Ermittler für spezifizierte Quelle die Anzahl der Linux Benutzer pro Land
Reduce Funktion: Summiere die Anzahl der Linux Benutzer pro Land
Input: kernel.org, archlinux.org, ubuntu.com, fsf.org, linux.com


== MAP FUNKTION ==
-> Wir benötigen fünf Instanzen der Map-Funktion, nämlich eine pro Quelle. Eine Instanz der Map-Funktion analyisiert als jeweils eine Quelle und liefert die Anzahl User pro Land. Folgend eine Auflistung aller Instanzen der Map-Funktion mit ihrem Input (in Klammern) und Output (Land: Anzahl Benutzer).

MAP(kernel.org) ->
  Schweiz: 11
  Deutschland: 56
  Italien: 32

MAP(archlinux.org) ->
  Deutschland: 254
  Lichtenstein: 23
  Italien: 101
  Schweiz: 76

MAP(ubuntu.com) ->
  Italien: 785
  Deutschland: 551
  Lichtenstein: 98

MAP(fsf.org) ->
  Italien: 43
  Schweiz: 23
  Deutschland: 71
  Lichtenstein: 42

MAP(linux.com) ->
  Lichtenstein: 23
  Deutschland: 77


== SHUFFLE ==
-> Das Framework gruppiert die Map Resultate nach Ländern.

Schweiz: 11, 76, 23
Deutschland: 56, 254, 551, 71, 77
Italien: 32, 101, 785, 43
Lichtenstein: 23, 98, 42, 23


== REDUCE FUNKTION ==
-> Wir benötigen vier Instanzen der Reduce Funktion, nämlich eine pro Land. Folgend eine Auflistung alle Instanzen der Reduce-Funktion. Als Parameter kriegen diese je ein Land un eine Liste der ermittelten Anzahl Benutzer. Die Reduce Funktion summiert die Anzahl der Benutzer und liefert für jedes Land die summierte Anzahl der Benutzer.

REDUCE(Schweiz, {11, 76, 23}) -> Schweiz: 110

REDUCE(Deutschland, {56, 254, 551, 71, 77}) -> Deutschland: 1009

REDUCE(Italien, {32, 101, 785, 43}) -> Italien: 961

REDUCE(Lichtenstein, {23, 98, 42, 23}) -> Lichtenstein: 186
