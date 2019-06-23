# Taty

## What is it?

Taty is a toy lisp written in java. It is about 1.2K lines of code and it implements the same
API described in [these](https://tbaldridge.pivotshare.com/media/lisp-in-x-ep1-a-lisp-in-clojure/71640/feature)
[two](https://tbaldridge.pivotshare.com/media/lisp-in-x-ep2-lisp-in-lisp-(in-lisp)/71641/feature)
tutorials by Tim Baldridge so you can run [this](https://tbaldridge.pivotshare.com/media/lisp-in-x-ep2-lisp-in-lisp-(in-lisp)/71641/feature)
fun exercise with just a couple minor path tweaks.


## Is it good?

[Yes.](https://news.ycombinator.com/item?id=3067434)


## Resources

These are some of the resources that inspired the creation of this project:

* [These](https://tbaldridge.pivotshare.com/media/lisp-in-x-ep1-a-lisp-in-clojure/71640/feature)
  [two](https://tbaldridge.pivotshare.com/media/lisp-in-x-ep2-lisp-in-lisp-(in-lisp)/71641/feature)
  video tutorials by Tim Baldridge
* [First](https://leanpub.com/readevalprintlove001/read) 
  [two](https://leanpub.com/readevalprintlove002) installments of Read-Eval-Print-Love by 
  Michael Fogus
* [JavaScript Allongé](https://leanpub.com/javascriptallongesix/read) book
* [Programming Languages](https://www.coursera.org/learn/programming-languages) coursera course
* [Essentials of Programming Languages](http://www.eopl3.com/) 3rd edition
* [Roots of Lisp](http://www.paulgraham.com/rootsoflisp.html) article by Paul Graham
* [Joy of Clojure](https://www.manning.com/books/the-joy-of-clojure-second-edition) 2nd edition
* [Rich Hickey talks](https://changelog.com/posts/rich-hickeys-greatest-hits)
* [Make a Lisp](https://github.com/kanaka/mal)


## Usage

### Getting an executable

If you are on linux or osx you can grab the graalvm built provided executables 
from [releases](https://github.com/kongeor/taty/releases).

Running without params will start a REPL session:

```js
➜ ./taty-0.1.0-linux
Welcome to Taty!
(\)=>
```

Passing one argument will evaluate the provided file:

```js
➜ ./taty-0.1.0-linux fact.taty
120
```


### Building 

Compile using the provided gradle wrapper:

```sh
./gradlew jar
```


## License

[MIT](LICENSE)








