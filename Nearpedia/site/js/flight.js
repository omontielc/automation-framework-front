(function ($) {

    $('form button').on('click', function(e){
        e.preventDefault();
        if(fieldsOK()){
            window.location.href = "results.html";
        }
    })

    let fieldsOK = function(){
        if(!originOk()){
            displayAlert("You must select an origin.");
            return false;
        }
        if(!destinationOk()){
            displayAlert("You must select a destination.");
            return false;
        }
        if(equalsOriginDestination()){
            displayAlert("Origin and destination can't be the same.");
            return false;
        }
        if(datesEmpty()){
            displayAlert("Dates can't be empty.");
            return false;
        }
        if(!datesCoherent()){
            displayAlert("Departing date should be minor than returning date.");
            return false;
        }
        return true;
    }

    let displayAlert = function toogleAlert(messageToDisplay){
        let message = alertMessage.formatUnicorn({message:messageToDisplay});
        $('#message').append(message);
        return false;
    }

    let datesEmpty = function(){
        return $('#departing').val() === '' || $('#returning').val() === '';
    }

    let datesCoherent = function(){
        return $('#departing').val() < $('#returning').val();
    }

    let originOk = function(){
        return $('#flight-from').children("option:selected").val() !== "-1";
    }

    let destinationOk = function(){
        return $('#flight-to').children("option:selected").val() !== "-1";
    }

    let equalsOriginDestination = function(){
        return $('#flight-to').children("option:selected").val() === $('#flight-from').children("option:selected").val();
    }

})(jQuery);

