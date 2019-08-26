Multiple-String-Searcher
============

[![Build Status](https://travis-ci.org/neo-search/multiple-string-searcher.svg?branch=master)](https://travis-ci.org/neo-search/neo-search)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0f65bfb641f745a4b301b85d028a4a8d)](https://www.codacy.com/app/bor-robert/aho-corasick)
[![Codecov](https://codecov.io/gh/neo-search/multiple-string-searcher/branch/master/graph/badge.svg)](https://codecov.io/gh/robert-bor/aho-corasick)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ahocorasick/ahocorasick/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.ahocorasick/ahocorasick)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/org.ahocorasick/ahocorasick/badge.svg)](http://www.javadoc.io/doc/org.ahocorasick/ahocorasick)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)


Introduction

Multiple-String-Searcher is an easy to use library to locate elements of a finit dictionary within an input text.
It matches all strings simultaneously and is very fast, even when used with dictionaries with several 100,000 entries.

Algorithms like Aho-Corasick or Commentz-Walter shines. First, they construct a optimized structure called a trie. 
When, they process the input text.
