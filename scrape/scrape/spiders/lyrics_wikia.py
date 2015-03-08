import scrapy
from bs4 import BeautifulSoup
import html2text


class LyricsWikiaSpider(scrapy.Spider):
    name = "lyricsmode"
    allowed_domains = ['lyricsmode.com']
    start_urls = ['http://www.lyricsmode.com/lyrics/t/taylor_swift/blank_space.html']

    def parse(self, response):
        lyrics = str(BeautifulSoup(response.body).find(id='lyrics_text')) \
            .encode('ascii', 'ignore')
        with open('lyrics', 'a') as f:
            h2t = html2text.HTML2Text()
            h2t.ignore_links = True
            f.write(h2t.handle(lyrics))
