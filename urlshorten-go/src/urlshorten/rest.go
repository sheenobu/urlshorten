package urlshorten

import "github.com/gin-gonic/gin"

type RestService struct {
	storage Storage
}

func NewRestService(st Storage) *RestService {
	return &RestService{
		storage: st,
	}
}

func (rest *RestService) Run() {
	g := gin.Default()

	g.GET("/urls/{slug}", rest.handleGet)

	g.Run(":3001")
}

func (rest *RestService) handleGet(c *gin.Context) {
	slug := c.Param("slug")

	url, err := rest.storage.Load(Slug(slug))
	if err != nil {
		c.JSON(500, gin.H{
			"error": err,
		})
		return
	}

	c.JSON(200, gin.H{
		"url":  url.URL.String(),
		"slug": slug,
	})
}
