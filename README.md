# NewsApp 
This is a simple app with Android MVVM architecture and LiveData Example
<blockquote>dependencies</blockquote>

- Koin
- Retrofit
- rxjava2
- epoxy

# Setting up
- Register a [NewsApi API key](https://newsapi.org/)
- Create a ```const.gradle``` file and code like this
```
def varNewsApiKey = "xxx" // NewsApi API key
project.ext{
    newsApiKey = varNewsApiKey
}
```
