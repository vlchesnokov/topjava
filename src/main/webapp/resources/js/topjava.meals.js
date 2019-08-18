const ajaxUrl = "ajax/profile/meals/";

function clearFilter() {
    $(".form-control").val('');
    updateTable();
}

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + 'filter',
        data: $('#filter').serialize()
    }).done(updateTableByData);
}

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: ajaxUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            }),
            updateTable: updateFilteredTable
        }
    );

    $.datetimepicker.setLocale(navigator.language);

//  http://xdsoft.net/jqplugins/datetimepicker/
    const startDate = $('#startDate');
    const endDate = $('#endDate');
    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
        formatDate: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });

    const startTime = $('#startTime');
    const endTime = $('#endTime');
    startTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        }
    });

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});