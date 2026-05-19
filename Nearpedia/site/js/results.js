(function ($) {
    var result = '<div class="row flight-card">' +
        '<div class="col-md-3"><span class="flight-time">{flighttime}</span><br/><a class="details" href="#">Details & baggage fees</a></div>' +
        '<div class="col-md-6"><span class="duration">{duration}</span><br/><span class="flight">{flight}</span><br/><span class="airline">{airline}</span></div>' +
        '<div class="col-md-2"><span class="price">USD ${price} </span><br/><span class="roundtrip">roundtrip</span></div>' +
        '<div class="col-md-1"><button class="btn btn-primary button-ns">Select</button></div>' +
        '</div>' +
        '</div>';

    var data = '[{"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"233"},{"time":"10:07am to - 12:07pm","duration":"4h 23m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"220"},{"time":"10:07am to - 12:07pm","duration":"3h 55m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"400"},{"time":"10:07am to - 12:07pm","duration":"4h 0m (One stop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"400"},{"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"180"},{"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"180"},{"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"180"},{"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"180"}, {"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"344"}, {"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"300"}, {"time":"10:07am to - 12:07pm","duration":"4h 0m (Nonstop)","flight":"MEX - HAV","airline":"Frontier Airlines 6966 operated by Operated By Volaris","price":"335"}]';

    $(document).ready(function () {
        disablePage();
        let flightData = loadFlights();

        buildFlightList(flightData);
    })

    $('#sort').on('change', function(){
        let sortBy = $(this). children("option:selected"). val();
        let flightData = loadFlights(true, sortBy);
        buildFlightList(flightData);
    })

    let startLoader = function(){
        
        $('.page-loader').preloader();
    }

    let stopLoader = function(){
        let timer = (Math.floor(Math.random() * (5 - 2 + 1) + 2)) * 1000;
        setTimeout(function(){ $('.page-loader').preloader('remove'); }, timer);
        setTimeout(function(){ enablePage(); }, timer);
        
    }

    let enablePage = function(){
        $('#sort').show();
        $('#results').show();
    }

    let disablePage = function(){
        $('#sort').hide();
        $('#results').hide();
    }

    let loadFlights = function(sorted = false, order = "desc"){

        startLoader();

        let flights = JSON.parse(data);

        if(sorted){
            if(order === 'desc'){
                flights.sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
                flights[8].price = 181;
            }
            else{
                flights.sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
            }
        }

        stopLoader();

        return flights
    }

    let buildFlightList = function(flightData){
        $('#results').html("");
        flightData.forEach(element => {
            $('#results').append(result.formatUnicorn({flighttime:element.time, duration: element.duration, flight:element.flight, airline: element.airline, price: element.price}));
        });
    }

})(jQuery);


