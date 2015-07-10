# What is this thing

This is a Twitter bot that looks for tweets that can be sung to the tune of Taylor Swift songs, and gives you the name of the song when it finds one. For example, the tweet "I wanna be in Alaska already" could be sung to the tune of Teardrops on my Guitar as "I wanna be / in Alaska already". Try it! What fun.

# How?

The much beloved (by me) [CMU Pronouncing Dictionary](http://www.speech.cs.cmu.edu/cgi-bin/cmudict) (cmudict) provides easily machine-readable pronunciations for a very good chunk of English. This gives us the ability to find rhymes, and a version of the dictionary augmented with syllable boundaries allows us to count syllables.

An unremarkable scrapy project finds T-Swift lyrics on the web. Using cmudict, we build a map of the (rough) type ````(line length, end rhyme) -> song name```` from these lyrics. Now, we can figure out the syllable count and end rhyme of a tweet and try to look it up in the map. If there's a match, hurray!

# Why?

In theory, with this bot active I won't need to sing the things people say to me back to them to the tune of Taylor Swift songs, because that will be handled for me. In practice I will probably keep doing it.

# Notes

The output of the scrapy project needs a bit of doctoring (s/22/twenty two), because cmudict, understandably, doesn't include numbers.
