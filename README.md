# Affiliate Link Crawler

This project was made within a day for the purpose of crawling the web in search of affiliate programs. This program was never made with the intention to be production ready and as such should not necessarily be trusted.

## How it works

When compiled (or ran) the program will ask for a bunch of starter links. Each time you enter a new link it will ask for another one. If you press ENTER without adding another link the program will start up. The program doesn't make sure the link you entered is correct and as such it is your responsibility to make sure you enter correct links. The program will spawn a separate thread per link given. The program will print all the links it found, to contain something about "Affiliate", to a text document, in the same directory, called "AffiliateLinks.txt".

## License

MIT