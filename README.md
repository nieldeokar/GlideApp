# GlideApp
Shot descriptive app on best practices and knows issues which i have faced during my development with [Glide][2]. read more [here][1].


### Grey/Green bg on images loaded via cache  
I have observed grey/green background on white images loaded via glide cache. 


### Using setTag() on glide targeted imageViews
Glide does not allows you to use setTag() method on imageView. Here is an alternative.


### invalidate cache of particular image.
Glide uses LRU cache so we can not invalidate cache of particalur imageView but we can set the expiry time or last update time which would reload image from server.

#### Libraries used
* AppCompat
* [Glide][2]



<a href="url"><img src="https://github.com/nieldeokar/GlideApp/blob/master/home_screen.png" align="left" height="800" width="500" ></a>


[1]: https://medium.com/@nieldeokar/using-glide-save-some-time-here-60f41e29d30a
[2]: https://github.com/bumptech/glide
