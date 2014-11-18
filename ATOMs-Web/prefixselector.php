<script type="text/javascript">
function show_title_other()
{

    if (document.getElementById('title').value == "other" ){
	document.getElementById('title_other').style.display = 'block';}
    else {document.getElementById('title_other').style.display = 'none';}

}
</script>

<select name="title" id="title" onchange="show_title_other(this.form);" class="form-control" style="padding: 0px; padding-left: 15px;">
	<option value="นาย">Please Select...</option>
	<option value="นาย">นาย</option>
	<option value="นาง">นาง</option>
	<option value="นางสาว">นางสาว</option>
	<option value="Mr.">MR</option>
	<option value="Mrs.">MRS</option>
	<option value="Ms.">MS</option>
	<option value="Miss">MISS</option>
	<option value="other">Other</option>
</select> <input type="text" name="title_other" id="title_other" class="form-control" style="display:none;" placeholder="Please speficify your prefix here" />
