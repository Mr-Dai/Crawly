# Crawly

Inspired by [webmagic](https://github.com/code4craft/webmagic), Crawly is an open source web crawler framework for Java which provides a fine-grained component structure. Using it, you can easily setup a web crawler for your production server.

Currently the project is still under construction and is way behind its first release.

## TODO

- [ ] Add support for Proxy (`java.net.Proxy`).
- [ ] Add support for other network protocols ([Apache Commons Net](http://commons.apache.org/proper/commons-net/index.html)).
- [ ] Write test cases for the existed code.
- [ ] Add more example crawlers.
- [ ] Distributed crawler.
- [x] Build up the basic structure of the framework.
- [x] Write default implementation for `Scheduler`.
- [x] Write default implementation for `Pipeline`.
- [x] Implement `Request`.
- [x] Write default implementation for `Downloader`.
- [x] Implement `Response`.
- [x] Write example crawlers.
- [x] Implement multi-thread support for `Crawler`.
- [x] Add distinct scheduler.
- [x] Implement bloom filter.
