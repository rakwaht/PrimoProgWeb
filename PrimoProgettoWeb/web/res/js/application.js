/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//test presenza di jquery
function myFunction()
{
$("#test").html("Hello jQuery");
}
$(document).ready(myFunction);


//funzione per linkare i file alla creazione dei post
function linka_selezionati(){
	var n = $( "input" ).lenght;
        alert(n);
 }