/**
 * parametric.js
 * Helps with the parametric search.
 *
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */

/**
 * Resets a list box by its ID.
 *
 * @param {String} list_id ID of the list box.
 */
function resetList(list_id) {
	var lst = document.getElementById(list_id);

	// Ignore if the list wasn't found.
	if (lst === null)
		return;

	// Go through each item setting them back to their default state.
	for (var j = 0; j < lst.options.length; j++) {
		lst.options[j].selected = lst.options[j].defaultSelected;
	}
}

/**
 * Resets list boxes by their ID.
 *
 * @param {Array} list_ids IDs of the list boxes.
 */
function resetLists(list_ids) {
	// Go though the list of list box IDs.
	for (var i = 0; i < list_ids.length; i++) {
		resetList(list_ids[i]);
	}
}

