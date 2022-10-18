# accommodation-api

To run app, inside app directory run:
on Linux: 
./gradlew bootRun
on Windows:
gradlew.bat bootRun

To test running app, you can use curl as follows:
curl --header "Content-Type: application/json" \
--request POST \
--data '{"economy":"1","premium":"2"}' \
http://localhost:8080/accommodation/bookingWithIncome