WORK IN PROGRESS. do NOT use.
=============================

Multiple-String-Searcher
========================

[![Build Status](https://travis-ci.org/neo-search/multiple-string-searcher.svg?branch=master)](https://travis-ci.org/neo-search/neo-search)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0f65bfb641f745a4b301b85d028a4a8d)](https://www.codacy.com/app/bor-robert/aho-corasick)
[![Codecov](https://codecov.io/gh/neo-search/multiple-string-searcher/branch/master/graph/badge.svg)](https://codecov.io/gh/robert-bor/aho-corasick)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ahocorasick/ahocorasick/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.ahocorasick/ahocorasick)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/org.ahocorasick/ahocorasick/badge.svg)](http://www.javadoc.io/doc/org.ahocorasick/ahocorasick)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)


Introduction
------------

Multiple-String-Searcher is a library to locate elements of a dictionary within an input text. It matches all strings simultaneously and is very fast, 
even when used with dictionaries with several 100,000 entries.

Multiple-String-Search
 - has a fluent API,
 - is easy to use,
 - it optimized for speed
 - implements several string search algorithms, that have different advantages for different situtations
 - allows to define payloads, that are returned for matched strings. This allows to realize a very simple and fast named entity recognition.

A lot of work was spent into making the algorithm fast under most circumstances.

multiple-string-search implements the following library:
 - an implementation of Aho-Corasick based on an implemententation from @robert-bot.  The algorithm is explained in great detail in the white paper written by Aho and Corasick: http://cr.yp.to/bib/1975/aho.pdf 
 - an implementation of Aho-Corasick based on an implemententation from @robert-bot, however not threat-safe and about 2-3 times faster. The user has to take care to not 
 add search strings from several libraries
 - an implementantion based on compressed tree / patricia trees

Usage
-----
Setting up the StringSearcher is easy: 

```java
    StringSearcher<String> searcher = StringSearcher.builder()
        .addSearchString("hers")
        .addSearchString("his")
        .addSearchString("she")
        .addSearchString("he")
        .build();
    Collection<Emit<String>> emits = searcher.parseText("ushers");
```


You can now read the input text. In this case it will find the following:
* "she" starting at position 1, ending at position 3
* "he" starting at position 2, ending at position 3
* "hers" starting at position 2, ending at position 5


In normal situations you probably want to remove overlapping instances, retaining the longest and left-most
matches.

```java
    StringSearcher searcher = StringSearcher.builder()
        .ignoreOverlaps()
        .addSearchString("hot")
        .addSearchString("hot chocolate")
        .build();
    Collection<Emit> emits = searcher.parseText("hot chocolate");
```


The ignoreOverlaps method tells the Trie to remove all overlapping matches. For this it relies on the following
conflict resolution rules: 1) longer matches prevail over shorter matches, 2) left-most prevails over right-most.
There is only one result now:
* "hot chocolate" starting at position 0, ending at position 12

If you want the algorithm to only check for whole words, you can tell the SearchTrie to do so:

```java
    StringSearcher trie = StringSearcher.builder()
        .onlyWholeWords()
        .addKeyword("sugar")
        .build();
    Collection<Emit> emits = searcher.parseText("sugarcane sugarcane sugar canesugar");    
```

In this case, it will only find one match, whereas it would normally find four. The sugarcane/canesugar words
are discarded because they are partial matches.

Some text is WrItTeN in a combination of lowercase and uppercase and therefore hard to identify. You can instruct
the Trie to lowercase the entire searchtext to ease the matching process. The lower-casing extends to keywords as well.

```java
    StringSearcher<?> stringSearcher = StringSearcher.builder()
        .ignoreCase()
        .addKeyword("casing")
        .build();
    Collection<Emit> emits = searcher.parseText("CaSiNg");
```

Normally, this match would not be found. With the ignoreCase settings the entire search text is lowercased
before the matching begins. Therefore it will find exactly one match. Since you still have control of the original
search text and you will know exactly where the match was, you can still utilize the original casing.

It is also possible to just ask whether the text matches any of the keywords, or just to return the first match it 
finds.

```java
    StringSearcher<?> stringSearcher = StringSearcher.builder().ignoreOverlaps()
            .addKeyword("ab")
            .addKeyword("cba")
            .addKeyword("ababc")
            .build();
    Emit firstMatch = StringSearcher.firstMatch("ababcbab");
```

The firstMatch will now be "ababc" found at position 0. containsMatch just checks if there is a firstMatch and
returns true if that is the case.

If you just want the barebones Aho-Corasick algorithm (ie, no dealing with case insensitivity, overlaps and whole
 words) and you prefer to add your own handler to the mix, that is also possible.
 
```java
    StringSearcher<?> stringSearcher = StringSearcher.builder()
            .addKeyword("hers")
            .addKeyword("his")
            .addKeyword("she")
            .addKeyword("he")
            .build();

    final List<Emit> emits = new ArrayList<>();
    EmitHandler emitHandler = new EmitHandler() {

        @Override
        public void emit(Emit emit) {
            emits.add(emit);
        }
    };
```

In many cases you may want to do useful stuff with both the non-matching and the matching text. In this case, you
might be better served by using the StringSearcher.tokenize(). It allows you to loop over the entire text and deal with
matches as soon as you encounter them. Let's look at an example where we want to highlight words from HGttG in HTML:

```java
    String speech = "The Answer to the Great Question... Of Life, " +
            "the Universe and Everything... Is... Forty-two,' said " +
            "Deep Thought, with infinite majesty and calm.";
    StringSearcher<?> stringSearcher = StringSearcher.builder().ignoreOverlaps().onlyWholeWords().ignoreCase()
        .addKeyword("great question")
        .addKeyword("forty-two")
        .addKeyword("deep thought")
        .build();
    Collection<Token> tokens = StringSearcher.tokenize(speech);
    StringBuffer html = new StringBuffer();
    html.append("<html><body><p>");
    for (Token token : tokens) {
        if (token.isMatch()) {
            html.append("<i>");
        }
        html.append(token.getFragment());
        if (token.isMatch()) {
            html.append("</i>");
        }
    }
    html.append("</p></body></html>");
    System.out.println(html);
```

You can also emit custom outputs. This might for example be useful to implement a trivial named entity 
recognizer. In this case use a PayloadTrie instead of a Trie:

```java
    class Word {
        private final String gender;
        
        public Word(String gender) {
            this.gender = gender;
        }
    }
    
    PayloadTrie<Word> trie = PayloadStringSearcher.<Word>builder()
        .addKeyword("hers", new Word("f")
        .addKeyword("his", new Word("m"))
        .addKeyword("she", new Word("f"))
        .addKeyword("he", new Word("m"))
        .build();
    Collection<PayloadEmit<Word>> emits = searcher.parseText("ushers");
```

Releases
--------
Information on the aho-corasick [releases](https://github.com/neo-search/multiple-string-searcher/releases).

License
-------
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
    
    