// эта функция принимает на вход ID HTML элемента и JSON массив данных. 
// она вставялет новые опции для селекта в текущий компонент. здесь она лишь объявляется (как метод в классе) - вызываться она будет позже
function initSelectElement(htmlElementId, jsonArray) {
    $('#' + htmlElementId).find('option').remove().end(); // удалить все
    // старые опции

    // вставить пустую опцию. в случае edit формы при начальной загрузке надо
    // будет подправить код, чтобы не перетиралоась выбранная опция. текущий
    // вариант только для нового объекта
    $('#' + htmlElementId).append($("<option></option>").attr({
        "disabled": '',
        "selected": '',
        "value": ''
    }).text(' -- select an option -- '));

    // вставляет новые опции в элемент
    // key - это index элемента по сути, а value - целый объект, представляющий наш DTO
    $.each(jsonArray, function (key, value) {
        $('#' + htmlElementId).append($("<option></option>").attr("value", value.id).text(value.title));
    });
}

function initComboboxes(contextUrl) {

    // $ - это ссылка на глобальный jquery Объект. через него можно делать все
    // "магические штуки", которые тут происходят.

    // делаем GET запрос на наш контроллер и пишем функцию для обработки
    // результата
    // запроса.
    // текущая строка кода выполнится при загрузке страницы (в соответствии с
    // местом встави в JSP)
    $.get(contextUrl + "/ajax-samples/countries", function (countriesArray) {

        // получив список стран - инициализируем соответствующий комбик
        initSelectElement('country', countriesArray);

        // вешаем функцию-обработчик на 'onchange' событие на нужные элементы.
        // получаем выбранный сейчас элемент и строим с его помощь. новый GET
        // запрос
        // на сервер
        $("#country").change(function () {
            var selectedId = $(this).val();
            $.get(contextUrl + "/ajax-samples/regions?countryId=" + selectedId, function (regionsArray) {
                initSelectElement('region', regionsArray);
            })
        });

        $("#region").change(function () {
            var selectedId = $(this).val();
            $.get(contextUrl + "/ajax-samples/cities?regionId=" + selectedId, function (citiesArray) {
                initSelectElement('city', citiesArray);
            })

        });
    });
    // документация по JQUERY - https://api.jquery.com/

    // вот к примеру, где я взял "как сделать GET запрос" -
    // https://api.jquery.com/get/

    // вот тут я подсмотрел как повесить onchange на элемент -
    // https://api.jquery.com/change/

    // вот тут -
    // https://stackoverflow.com/questions/8605516/default-select-option-as-blank
    // -
    // как сгенерить пустую опцию в селекте, чтобы оно не показывало первую из
    // списка (это неправильно)

    // а вот тут -
    // https://stackoverflow.com/questions/47824/how-do-you-remove-all-the-options-of-a-select-box-and-then-add-one-option-and-se
    // - как почистить список опция, чтобы установить новый

    // Итого: я знаю только факт существования JQuery, хорошо знаю JS (но на
    // достаточном уровне его можно за 1 день узнать), а весь результат - это
    // разбивка задачи на кусочки и помощь гугла. в таком же стиле пробуйте
    // решщать
    // остальные похожие

}