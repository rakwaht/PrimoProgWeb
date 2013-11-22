/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




//funzione per linkare i file alla creazione dei post
function linka_selezionati() {
    $('#scelte :checked').each(function() {
        var link = '$$' + $(this).val().toString() + '$$';
        var textarea = $('#testo_post');
        textarea.val(textarea.val() + ' ' + link);
    });
}


  


//aggiungo dinamicamente input file alla crea post
function add_upload_file() {
    $('form input:file').last().after($('<br /><input type="file" name="file" class="file" onchange="add_upload_file();" />'));
}
;



