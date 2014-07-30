express = require 'express'
app = express()
app.use express.static(process.env.DOCROOT)
app.listen process.env.PORT
