 Manash 2019-12-06:

https://github.com/bioatlas/spatial-service/blob/master/.travis.yml
https://github.com/AtlasOfLivingAustralia/spatial-service/blob/feature/docker-and-encoding-fixes/.travis.yml


0. fix the .travis.yml-file ( use quotes around the variable $HEMLIGT )
1.   api_key:
    secure: "$HEMLIGT" 
2. where HEMLIGT is set in travis -> created in github, copied to travis ...
3. github.com -> user-profile -> 'settings' -> 'developer settings' -> 'personal access token'

git config --global push.followTags true
git tag -a v3.1.vega -m "testing "
git push --tags

Travis says when everything works :
Logged in as Ingi Erli
Deploying to repo: dina-web-nrm/mediaserver-module
Current tag is: v3.1.vega
Setting target_commitish to 4a10fcab0f91732366fc3d63a530adb665caa6ee
Preparing deploy
Deploying application
Done. Your build exited with 0.