# h2gis-custom-functions

## Beschreibung

Zusatzfunktionen für _h2gis_.

## Benutzerdokumention

Die Funktionen müssen _h2gis_ mittels `CREATE ALIAS` registriert werden, z.B.:

```
CREATE ALIAS IF NOT EXISTS SOGIS_RemoveInnerRings FOR "ch.so.agi.h2.functions.RemoveInnerRings.removeInnerRings";
```

Anschliessend können sie wie gewöhnliche Funktionen aufgerufen werden:

```
UPDATE BAUZONENGRENZEN_BAUZONENGRENZE SET geometrie = SOGIS_RemoveInnerRings(GEOMETRIE, 1.0)
```

Die Funktionen müssen im Klassenpfad sein.

## Developing

Es müssen statische Klassen sein.

Jeder Commit stösst die Github-Action Pipeline an. Ist der Build und das Testen erfolgreich, wird die Jar-Datei auf Bintray deployed.