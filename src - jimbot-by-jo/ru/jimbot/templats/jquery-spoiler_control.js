		function BspoilerControl(){
			//�������� ��� ��������
			$("div[name='spoiler']").hide();
			//���� ������� ��������� ������ ����� � �������� (������ ��� ������������� �����������)
			$("p[name='spoilerbutton']").html("<img src='img/interface/but_more.gif'>&nbsp;��������");
			//���������� ��� ������ ��������� �� ��������
			$("p[name='spoilerbutton']").each(function(){
				//���� ����� ����� ������� �������� ����� ���������
				if($(this).prev(this).get(0).tagName == "H1" || $(this).prev(this).get(0).tagName == "H2" || $(this).prev(this).get(0).tagName == "H3"){
					//����� �� ��������� ��������� � ������, � ��� ��������� �������
					var NewSpoilerButton = "<div name='spoilerbutton' class='advSpoilerHeader'><p><span>[+]</span><b>"+$(this).prev(this).html()+"</b> </p></div>";
					$(this).prev(this).replaceWith("");
					$(this).replaceWith(NewSpoilerButton);
					//� ���������� ������ ��������� � ����������� ���������� div-���. ������ �� �������� ����� ���� 2 ���� ������ ��������� p � div ����
				}
			});
			//��� ���� div ������ ������������ ����
			$("div[name='spoilerbutton']").click(function () {
				//���� ������� �����. ��������: ���� �������� display ��� ������� DOM �����/������ ����� "block" (�.�. ����� � ��������� ������������ ������� slide)
				if($(this).next(this).css("display")=="block"){
					//���������� "��������>>" � span, ������� ������� p, ������� ������� ����� ������ this. ����� �������� ��� �� "<<������"
					$(this).children("p").children("span").html("[+]");
					//����������� �������� �������
					$(this).next(this).slideUp("normal");
				} else {
					//���� ������� �� ������, �� �� ������ � �������������� ������ ������� � ������������� �������
					$(this).children("p").children("span").html("[-]");				
					$(this).next(this).slideDown("normal");
				}
			  return false;
			 });		
			//������ ���� ��� ���� p-������
			$("p[name='spoilerbutton']").click(function () {
				//�� ���� �� ��������, ��� �� �����, ������ �����
				if($(this).next(this).css("display")=="block"){
					$(this).next(this).slideUp("normal");
					$(this).html("<img src='img/interface/but_more.gif'>&nbsp;��������");
				} else {
					$(this).next(this).slideDown("normal");
					$(this).html("<img src='img/interface/but_more.gif'>&nbsp;������");
				}
			  return false;
			 });
		}
	 

		
	  
	