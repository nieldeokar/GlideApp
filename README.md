# GlideApp
Shot descriptive app on best practices and knows issues which i have faced during my development. read more here.


### Grey/Green bg on images loaded via cache  
I have observed grey/green background on white images loaded via glide cache. 


### Using setTag() on glide targeted imageViews
Glide does not allows you to use setTag() method on imageView. Here is an alternative.


### invalidate cache of particular image.
Glide uses LRU cache so we can not invalidate cache of particalur imageView but we can set the expiry time or last update time which would reload image from server.



