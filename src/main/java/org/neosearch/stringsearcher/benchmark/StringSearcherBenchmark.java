package org.neosearch.stringsearcher.benchmark;

import java.util.Collection;

import org.neosearch.stringsearcher.StringSearcher;
import org.neosearch.stringsearcher.Token;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;

public class StringSearcherBenchmark {

    private static String DATA_FOR_TESTING = createData();

    private static StringSearcher<String> stringSearcherWithSomeKeywords = createStringSearcherWithSomeKeywords();

    private static StringSearcher<String> stringSearcherWith1000Keywords = createStringSearcherWith1000Keywords();

    private static StringSearcher<String> createStringSearcherWithSomeKeywords() {
        StringSearcher<String> searcher = StringSearcher.builder().addSearchStrings("BGB", "ZPO", "an dessen Feststellungen ")
                .build();
        return searcher;
    }

    private static StringSearcher<String> createStringSearcherWith1000Keywords() {
        StringSearcher<String> searcher = StringSearcher.builder().addSearchStrings("der", "die", "und", "in", "den", "von", "zu",
                "das", "mit", "sich", "des", "auf", "für", "ist", "im", "dem", "nicht", "ein", "Die", "eine", "als", "auch", "es",
                "an", "werden", "aus", "er", "hat", "daß", "sie", "nach", "wird", "bei", "einer", "Der", "um", "am", "sind",
                "noch", "wie", "einem", "über", "einen", "Das", "so", "Sie", "zum", "war", "haben", "nur", "oder", "aber", "vor",
                "zur", "bis", "mehr", "durch", "man", "sein", "wurde", "sei", "In", "Prozent", "hatte", "kann", "gegen", "vom",
                "können", "schon", "wenn", "habe", "seine", "Mark", "ihre", "dann", "unter", "wir", "soll", "ich", "eines", "Es",
                "Jahr", "zwei", "Jahren", "diese", "dieser", "wieder", "keine", "Uhr", "seiner", "worden", "Und", "will",
                "zwischen", "Im", "immer", "Millionen", "Ein", "was", "sagte", "Er", "gibt", "alle", "DM", "diesem", "seit",
                "muß", "wurden", "beim", "doch", "jetzt", "waren", "drei", "Jahre", "Mit", "neue", "neuen", "damit", "bereits",
                "da", "Auch", "ihr", "seinen", "müssen", "ab", "ihrer", "Nach", "ohne", "sondern", "selbst", "ersten", "nun",
                "etwa", "Bei", "heute", "ihren", "weil", "ihm", "seien", "Menschen", "Deutschland", "anderen", "werde", "Ich",
                "sagt", "Wir", "Eine", "rund", "Für", "Aber", "ihn", "Ende", "jedoch", "Zeit", "sollen", "ins", "Wenn", "So",
                "seinem", "uns", "Stadt", "geht", "Doch", "sehr", "hier", "ganz", "erst", "wollen", "Berlin", "vor allem",
                "sowie", "hatten", "kein", "deutschen", "machen", "lassen", "Als", "Unternehmen", "andere", "ob", "dieses",
                "steht", "dabei", "wegen", "weiter", "denn", "beiden", "einmal", "etwas", "Wie", "nichts", "allerdings", "vier",
                "gut", "viele", "wo", "viel", "dort", "alles", "Auf", "wäre", "SPD", "kommt", "vergangenen", "denen", "fast",
                "fünf", "könnte", "nicht nur", "hätten", "Frau", "Am", "dafür", "kommen", "diesen", "letzten", "zwar", "Diese",
                "großen", "dazu", "Von", "Mann", "Da", "sollte", "würde", "also", "bisher", "Leben", "Milliarden", "Welt",
                "Regierung", "konnte", "ihrem", "Frauen", "während", "Land", "zehn", "würden", "stehen", "ja", "USA", "heißt",
                "dies", "zurück", "Kinder", "dessen", "ihnen", "deren", "sogar", "Frage", "gewesen", "erste", "gab", "liegt",
                "gar", "davon", "gestern", "geben", "Teil", "Polizei", "dass", "hätte", "eigenen", "kaum", "sieht", "große",
                "Denn", "weitere", "Was", "sehen", "macht", "Angaben", "weniger", "gerade", "läßt", "Geld", "München", "deutsche",
                "allen", "darauf", "wohl", "später", "könne", "deshalb", "aller", "kam", "Arbeit", "mich", "gegenüber",
                "nächsten", "bleibt", "wenig", "lange", "gemacht", "Wer", "Dies", "Fall", "mir", "gehen", "Berliner", "mal",
                "Weg", "CDU", "wollte", "sechs", "keinen", "Woche", "dagegen", "alten", "möglich", "gilt", "erklärte", "müsse",
                "Dabei", "könnten", "Geschichte", "zusammen", "finden", "Tag", "Art", "erhalten", "Man", "Dollar", "Wochen",
                "jeder", "nie", "bleiben", "besonders", "Jahres", "Deutschen", "Den", "Zu", "zunächst", "derzeit", "allein",
                "deutlich", "Entwicklung", "weiß", "einige", "sollten", "Präsident", "geworden", "statt", "Bonn", "Platz",
                "inzwischen", "Nur", "Freitag", "Um", "pro", "seines", "Damit", "Montag", "Europa", "schließlich", "Sonntag",
                "einfach", "gehört", "eher", "oft", "Zahl", "neben", "hält", "weit", "Partei", "meisten", "Thema", "zeigt",
                "Politik", "Aus", "zweiten", "Januar", "insgesamt", "je", "mußte", "Anfang", "hinter", "ebenfalls", "ging",
                "Mitarbeiter", "darüber", "vielen", "Ziel", "darf", "Seite", "fest", "hin", "erklärt", "Namen", "Haus", "An",
                "Frankfurt", "Gesellschaft", "Mittwoch", "damals", "Dienstag", "Hilfe", "Mai", "Markt", "Seit", "Tage",
                "Donnerstag", "halten", "gleich", "nehmen", "solche", "Entscheidung", "besser", "alte", "Leute", "Ergebnis",
                "Samstag", "Daß", "sagen", "System", "März", "tun", "Monaten", "kleinen", "lang", "Nicht", "knapp", "bringen",
                "wissen", "Kosten", "Erfolg", "bekannt", "findet", "daran", "künftig", "wer", "acht", "Grünen", "schnell",
                "Grund", "scheint", "Zukunft", "Stuttgart", "bin", "liegen", "politischen", "Gruppe", "Rolle", "stellt", "Juni",
                "sieben", "September", "nämlich", "Männer", "Oktober", "Mrd", "überhaupt", "eigene", "Dann", "gegeben",
                "Außerdem", "Stunden", "eigentlich", "Meter", "ließ", "Probleme", "vielleicht", "ebenso", "Bereich",
                "zum Beispiel", "Bis", "Höhe", "Familie", "Während", "Bild", "Ländern", "Informationen", "Frankreich", "Tagen",
                "schwer", "zuvor", "Vor", "genau", "April", "stellen", "neu", "erwartet", "Hamburg", "sicher", "führen", "Mal",
                "Über", "mehrere", "Wirtschaft", "Mio", "Programm", "offenbar", "Hier", "weiteren", "natürlich", "konnten",
                "stark", "Dezember", "Juli", "ganze", "kommenden", "Kunden", "bekommen", "eben", "kleine", "trotz", "wirklich",
                "Lage", "Länder", "leicht", "gekommen", "Spiel", "laut", "November", "kurz", "politische", "führt", "innerhalb",
                "unsere", "meint", "immer wieder", "Form", "Münchner", "AG", "anders", "ihres", "völlig", "beispielsweise",
                "gute", "bislang", "August", "Hand", "jede", "GmbH", "Film", "Minuten", "erreicht", "beide", "Musik", "Kritik",
                "Mitte", "Verfügung", "Buch", "dürfen", "Unter", "jeweils", "einigen", "Zum", "Umsatz", "spielen", "Daten",
                "welche", "müßten", "hieß", "paar", "nachdem", "Kunst", "Euro", "gebracht", "Problem", "Noch", "jeden", "Ihre",
                "Sprecher", "recht", "erneut", "längst", "europäischen", "Sein", "Eltern", "Beginn", "besteht", "Seine",
                "mindestens", "machte", "Jetzt", "bietet", "außerdem", "Bürger", "Trainer", "bald", "Deutsche", "Schon", "Fragen",
                "klar", "Durch", "Seiten", "gehören", "Dort", "erstmals", "Februar", "zeigen", "Titel", "Stück", "größten", "FDP",
                "setzt", "Wert", "Frankfurter", "Staat", "möchte", "daher", "wolle", "Bundesregierung", "lediglich", "Nacht",
                "Krieg", "Opfer", "Tod", "nimmt", "Firma", "zuletzt", "Werk", "hohen", "leben", "unter anderem", "Dieser",
                "Kirche", "weiterhin", "gebe", "gestellt", "Mitglieder", "Rahmen", "zweite", "Paris", "Situation", "gefunden",
                "Wochenende", "internationalen", "Wasser", "Recht", "sonst", "stand", "Hälfte", "Möglichkeit", "versucht",
                "blieb", "junge", "Mehrheit", "Straße", "Sache", "arbeiten", "Monate", "Mutter", "berichtet", "letzte", "Gericht",
                "wollten", "Ihr", "zwölf", "zumindest", "Wahl", "genug", "Weise", "Vater", "Bericht", "amerikanischen", "hoch",
                "beginnt", "Wort", "obwohl", "Kopf", "spielt", "Interesse", "Westen", "verloren", "Preis", "Erst", "jedem",
                "erreichen", "setzen", "spricht", "früher", "teilte", "Landes", "zudem", "einzelnen", "bereit", "Blick", "Druck",
                "Bayern", "Kilometer", "gemeinsam", "Bedeutung", "Chance", "Politiker", "Dazu", "Zwei", "besten", "Ansicht",
                "endlich", "Stelle", "direkt", "Beim", "Bevölkerung", "Viele", "solchen", "Alle", "solle", "jungen", "Einsatz",
                "richtig", "größte", "sofort", "neuer", "ehemaligen", "unserer", "dürfte", "schaffen", "Augen", "Rußland",
                "Internet", "Allerdings", "Raum", "Mannschaft", "neun", "kamen", "Ausstellung", "Zeiten", "Dem", "einzige",
                "meine", "Nun", "Verfahren", "Angebot", "Richtung", "Projekt", "niemand", "Kampf", "weder", "tatsächlich",
                "Personen", "dpa", "Heute", "geführt", "Gespräch", "Kreis", "Hamburger", "Schule", "guten", "Hauptstadt",
                "durchaus", "Zusammenarbeit", "darin", "Amt", "Schritt", "meist", "groß", "zufolge", "Sprache", "Region",
                "Punkte", "Vergleich", "genommen", "gleichen", "du", "Ob", "Soldaten", "Universität", "verschiedenen", "Kollegen",
                "neues", "Bürgermeister", "Angst", "stellte", "Sommer", "danach", "anderer", "gesagt", "Sicherheit", "Macht",
                "Bau", "handelt", "Folge", "Bilder", "lag", "Osten", "Handel", "sprach", "Aufgabe", "Chef", "frei", "dennoch",
                "DDR", "hohe", "Firmen", "bzw", "Koalition", "Mädchen", "Zur", "entwickelt", "fand", "Diskussion", "bringt",
                "Deshalb", "Hause", "Gefahr", "per", "zugleich", "früheren", "dadurch", "ganzen", "abend", "erzählt", "Streit",
                "Vergangenheit", "Parteien", "Verhandlungen", "jedenfalls", "gesehen", "französischen", "Trotz", "darunter",
                "Spieler", "forderte", "Beispiel", "Meinung", "wenigen", "Publikum", "sowohl", "meinte", "mag", "Auto", "Lösung",
                "Boden", "Einen", "Präsidenten", "hinaus", "Zwar", "verletzt", "weltweit", "Sohn", "bevor", "Peter", "mußten",
                "keiner", "Produktion", "Ort", "braucht", "Zusammenhang", "Kind", "Verein", "sprechen", "Aktien", "gleichzeitig",
                "London", "sogenannten", "Richter", "geplant", "Italien", "Mittel", "her", "freilich", "Mensch", "großer",
                "Bonner", "wenige", "öffentlichen", "Unterstützung", "dritten", "nahm", "Bundesrepublik", "Arbeitsplätze",
                "bedeutet", "Feld", "Dr.", "Bank", "oben", "gesetzt", "Ausland", "Ministerpräsident", "Vertreter", "z.B.",
                "jedes", "ziehen", "Parlament", "berichtete", "Dieses", "China", "aufgrund", "Stellen", "warum", "Kindern",
                "heraus", "heutigen", "Anteil", "Herr", "Öffentlichkeit", "Abend", "Selbst", "Liebe", "Neben", "rechnen", "fällt",
                "New York", "Industrie", "WELT", "Stuttgarter", "wären", "Vorjahr", "Sicht", "Idee", "Banken", "verlassen",
                "Leiter", "Bühne", "insbesondere", "offen", "stets", "Theater", "ändern", "entschieden", "Staaten", "Experten",
                "Gesetz", "Geschäft", "Tochter", "angesichts", "gelten", "Mehr", "erwarten", "läuft", "fordert", "Japan", "Sieg",
                "Ist", "Stimmen", "wählen", "russischen", "gewinnen", "CSU", "bieten", "Nähe", "jährlich", "Bremen", "Schüler",
                "Rede", "Funktion", "Zuschauer", "hingegen", "anderes", "Führung", "Besucher", "Drittel", "Moskau", "immerhin",
                "Vorsitzende", "Urteil", "Schließlich", "Kultur", "betonte", "mittlerweile", "Saison", "Konzept", "suchen",
                "Zahlen", "Roman", "Gewalt", "Köln", "gesamte", "indem", "EU", "Stunde", "ehemalige", "Auftrag", "entscheiden",
                "genannt", "tragen", "Börse", "langen", "häufig", "Chancen", "Vor allem", "Position", "alt", "Luft", "Studenten",
                "übernehmen", "stärker", "ohnehin", "zeigte", "geplanten", "Reihe", "darum", "verhindern", "begann", "Medien",
                "verkauft", "Minister", "wichtig", "amerikanische", "sah", "gesamten", "einst", "verwendet", "vorbei", "Behörden",
                "helfen", "Folgen", "bezeichnet"

        ).build();
        return searcher;
    }

    // Benchmark code

    private static String createData() {
        String tmp = "2\nb) Dieser Verpflichtung zu einer insbesondere die näheren Umstände der Schenkung "
                + "berücksichtigenden Gesamtwürdigung wird das Berufungsurteil nicht gerecht. Die Würdigung des festgestellten Sachverhalts "
                + "ist zwar grundsätzlich Sache des Tatrichters, an dessen Feststellungen das Revisionsgericht gemäß § 559 Abs. 2 ZPO gebunden ist."
                + " Das Revisionsgericht kann lediglich nachprüfen, ob sich der Tatrichter entsprechend dem Gebot des § 286 ZPO mit dem Prozessstoff"
                + " und den Beweisergebnissen umfassend und widerspruchsfrei auseinandergesetzt hat (ständige Rechtsprechung, vgl."
                + " BGHZ 145, 35, 38; BGH, Urteil vom 14. Dezember 2004 - X ZR 3/03, FamRZ 2005, 511). Dieser Prüfung hält die Würdigung "
                + "des Berufungsgerichts aber nicht stand.";

        String data = "";

        for (int i = 0; i < 10000; i++)
            data += tmp;
        return data;
    }

    @Benchmark
    @Warmup(iterations = 2)
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    public void measureTimeWithSomeKeywords() {
        Collection<Token<String>> tokenize = stringSearcherWithSomeKeywords.tokenize(DATA_FOR_TESTING);
//        System.out.println(tokenize);

    }

    @Benchmark
    @Warmup(iterations = 2)
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    public void measureTimeWith1000Keywords() {
        Collection<Token<String>> tokenize = stringSearcherWith1000Keywords.tokenize(DATA_FOR_TESTING);
//        System.out.println(tokenize);

    }
}
