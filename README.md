# Stockhawk <img src="https://github.com/DaceyE/StockHawk/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" height="48px" />

<div>
  <img height="400" src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/zr/Screenshot_2018-12-19-21-21-57.png" />
  <img height="400" hspace="20"  src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/zr/Screenshot_2018-12-17-10-37-47.png" />
</div>
&nbsp
<img height="400" src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/kf/Screenshot_2018-12-19-20-07-20.png" />

**S**ee the bottom of this readme for more images.

## Summary
**T**his app is about doing rudimentary debugging on already existing application written by a different developer and adding the final polish to an app before release.  Additional features are added like a widget, graphs of stock data, and persisting stocks to display how they’ve changed over time.  Adding messages about loss of network connectivity, out of date data, and invalid user input.  There are a number of libraries used in this app and I’m not fond of all of them, but using software selected by a different developer was an import part of this project.


## Specifications
**Common Requirements**  
•  Follow Java style guide, git style guide, [core app quality guide][1], and [tablet app quality guide][2].  

**Required Components**  
•  Each stock quote on the main screen is clickable and leads to a new screen which graphs the stock's value over time.  
•  Stock Hawk does not crash when a user searches for a non-existent stock.  
•  Stock Hawk Stocks can be displayed in a collection widget.  
•  Stock Hawk app has content descriptions for all buttons for accessibility support.  
•  Stock Hawk app supports layout mirroring using both the RTL attribute and the start/end tags.  
•  Strings are all included in the strings.xml file and untranslatable strings have a translatable tag marked to false.  

**Libraries**  
•  App using previously selected libraries for development to handle networking, data persistence, and user interface.  


## Issues
•  Yahoo's public API is no longer available.  May replace it or make a fake verson.  Unless I use this app to demonstrate adding Android architecture components to an old app this likely won't happen.
•  Repo should be merged with the other demo applications for convenience.  
•  Upload Java style guide used.  
•  Upload Git style guide used.  
•  The above guides should be written as an html doc styled like the Android developer docs.  
•  Add RTL images.  
•  Add better app widget images.  


## Images
**S**creenshots are from a API 16 ZTE Reef and a 7th generation Kindle Fire because device diversity is important.

<div>
  <img height="240" src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/zr/Screenshot_2018-12-17-10-36-46.png" />
  <img height="240" hspace="20"  src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/zr/Screenshot_2018-12-19-10-18-51.png" />
</div>
&nbsp
<div>
  <img height="640" src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/kf/Screenshot_2018-12-19-21-11-58.png" />
  <img height="400" hspace="20"  src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/zr/Screenshot_2018-12-19-21-25-00.png" />
</div>
&nbsp
<img height="400" src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/kf/Screenshot_2018-12-19-21-12-14.png" />
&nbsp

<div>
  <img height="640" src="https://ennis.000webhostapp.com/portfolio/stock%20hawk/kf/Screenshot_2018-12-19-20-06-49.png" />
  <img height="300" src="https://raw.githubusercontent.com/DaceyE/StockHawk/master/app/src/main/mystocks_list_widget_preview-web.png" />
</div>


[launcher]: https://github.com/DaceyE/StockHawk/blob/master/app/src/main/res/mipmap-mdpi/ic_launcher.png
[1]: https://developer.android.com/docs/quality-guidelines/core-app-quality
[2]: https://developer.android.com/docs/quality-guidelines/tablet-app-quality
