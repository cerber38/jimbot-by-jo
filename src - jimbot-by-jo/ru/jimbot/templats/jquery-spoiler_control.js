		function BspoilerControl(){
			//Скрываем все спойлеры
			$("div[name='spoiler']").hide();
			//Всем кнопкам спойлеров задаем текст и картинку (удобно для многоязыковых интерфейсов)
			$("p[name='spoilerbutton']").html("<img src='img/interface/but_more.gif'>&nbsp;Показать");
			//Перебераем все кнопки спойлеров на странице
			$("p[name='spoilerbutton']").each(function(){
				//Если прямо перед кнопкой спойлера стоит заголовок
				if($(this).prev(this).get(0).tagName == "H1" || $(this).prev(this).get(0).tagName == "H2" || $(this).prev(this).get(0).tagName == "H3"){
					//Текст из заголовка переносим в кнопку, а сам заголовок убераем
					var NewSpoilerButton = "<div name='spoilerbutton' class='advSpoilerHeader'><p><span>[+]</span><b>"+$(this).prev(this).html()+"</b> </p></div>";
					$(this).prev(this).replaceWith("");
					$(this).replaceWith(NewSpoilerButton);
					//В результате кнопки спойлеров с заголовками становятся div-ами. Теперь на странице может быть 2 типа кнопок спойлеров p и div типа
				}
			});
			//Для всех div кнопок обрабатываем клик
			$("div[name='spoilerbutton']").click(function () {
				//Если спойлер видим. Дословно: если свойство display для первого DOM брата/сестры равно "block" (т.е. видим в контексте применяемого эффекта slide)
				if($(this).next(this).css("display")=="block"){
					//Записываем "показать>>" в span, который ребенок p, который ребенок нашей кнопки this. Потом поменяем его на "<<скрыть"
					$(this).children("p").children("span").html("[+]");
					//Сворачиваем открытый спойлер
					$(this).next(this).slideUp("normal");
				} else {
					//Если спойлер не открыт, то он закрыт и соответственно меняем надпись и разворачиваем спойлер
					$(this).children("p").children("span").html("[-]");				
					$(this).next(this).slideDown("normal");
				}
			  return false;
			 });		
			//Теперь клик для всех p-кнопок
			$("p[name='spoilerbutton']").click(function () {
				//По тому же принципу, что до этого, только проще
				if($(this).next(this).css("display")=="block"){
					$(this).next(this).slideUp("normal");
					$(this).html("<img src='img/interface/but_more.gif'>&nbsp;Показать");
				} else {
					$(this).next(this).slideDown("normal");
					$(this).html("<img src='img/interface/but_more.gif'>&nbsp;Скрыть");
				}
			  return false;
			 });
		}
	 

		
	  
	