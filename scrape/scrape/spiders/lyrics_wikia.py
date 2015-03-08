from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors import LinkExtractor
from bs4 import BeautifulSoup
import html2text


song_delimiter = '\n%%%\n'


def parse_song(response):
    soup = BeautifulSoup(response.body)
    lyrics = str(soup.find(class_='lyrics')) \
        .decode('ascii', 'ignore') \
        .encode('ascii', 'ignore')
    with open('lyrics', 'a') as f:
        h2t = html2text.HTML2Text()
        h2t.ignore_links = True
        f.write("title: {}\n\n".format(
            soup.find(class_='text_title').string.strip()))
        f.write(h2t.handle(lyrics))
        f.write(song_delimiter)


class GeniusCrawler(CrawlSpider):
    name = "genius"
    allowed_domains = ['genius.com']
    download_delay = 2
    start_urls = ['http://genius.com/albums/Taylor-swift/Fearless',
                  'http://genius.com/albums/Taylor-swift/Taylor-swift',
                  'http://genius.com/albums/Taylor-swift/Speak-now',
                  'http://genius.com/albums/Taylor-swift/Red',
                  'http://genius.com/albums/Taylor-swift/1989']
    rules = [Rule(
        link_extractor=LinkExtractor(
            restrict_xpaths='//div[contains(concat(" ", normalize-space(@class), " "), " album_tracklist ")]//a[contains(concat(" ", normalize-space(@class), " "), " song_name ")]'),
        callback=parse_song,
        follow=False)]
