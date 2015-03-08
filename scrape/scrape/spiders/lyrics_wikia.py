import scrapy


class LyricsWikiaSpider(scrapy.Spider):
    name = "lyrics.wikia"
    allowed_domains = ['lyrics.wikia.com']
    start_urls = ['http://lyrics.wikia.com/Taylor_Swift']

    def parse(self, response):
        pass
