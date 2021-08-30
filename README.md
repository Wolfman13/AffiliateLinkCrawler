# Affiliate Link Crawler

This project was made within a day for the purpose of crawling the web in search of affiliate programs. This program was never made with the intention to be production ready and as such should not necessarily be trusted.

## How it works

When compiled (or ran) the program will start by trying to open a file called "config.txt" (look at the example given in the repo). Note about the config file: The depth level is how deep you want the bot to dig. If the depth level is -1 the program will ignore the depth level completely. The program will spawn a separate thread per link given. The program will print all the links it found, to contain something about "Affiliate", to a text document, in the same directory, called "AffiliateLinks.txt".

## License

MIT