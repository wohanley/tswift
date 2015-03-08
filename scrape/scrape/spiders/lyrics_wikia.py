from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors import LinkExtractor
from bs4 import BeautifulSoup
import html2text


song_delimiter = '\n%%%\n'


def parse_song(response):
    soup = BeautifulSoup(response.body)
    lyrics = str(soup.find(id='lyrics_text')) \
        .decode('ascii', 'ignore') \
        .encode('ascii', 'ignore')
    with open('lyrics', 'a') as f:
        h2t = html2text.HTML2Text()
        h2t.ignore_links = True
        f.write("title: {}\n\n".format(
            soup.find(class_='header-song-name').string[:-7]))
        f.write(h2t.handle(lyrics))
        f.write(song_delimiter)


class LyricsWikiaSpider(CrawlSpider):
    name = "lyricsmode"
    allowed_domains = ['lyricsmode.com']
    download_delay = 2
    start_urls = ['http://www.lyricsmode.com/lyrics/t/taylor_swift/']
    rules = [Rule(
        link_extractor=LinkExtractor(
            restrict_xpaths='//div[contains(concat(" ", normalize-space(@class), " "), " all-lyrics ")]//a[contains(concat(" ", normalize-space(@class), " "), " ui-song-title ")]'),
        callback=parse_song,
        follow=False)]
