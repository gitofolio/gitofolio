<%@ page language="java" contentType="image/svg+xml; charset=UTF-8" pageEncoding = "UTF-8"%>

 <svg width="${svgDTO.getViewBoxWidth()}" height="${svgDTO.getViewBoxHeight()}" viewBox="0 0 ${svgDTO.getViewBoxWidth()} ${svgDTO.getViewBoxHeight()}" fill="none" xmlns="http://www.w3.org/2000/svg"
        xmlns:xlink="http://www.w3.org/1999/xlink">
	 
	 <style type="text/css">
		<![CDATA[
		@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;400;700&display=swap');
		 
		@keyframes delayFadeInAnimation{
			0%, 60%{opacity:0;}
			100%{opacity:1;}
		}
		
		@keyframes arrowAnimation{
			0%, 20%, 50%, 80%, 100% {transform: translateX(0);}
			40% {transform: translateX(-3px);}
			60% {transform: translateX(-1px);}
		} 
		 
		.userName {
			fill : ${svgDTO.getColor().getTextColor()};
            font : 700 18px Noto Sans KR;
			animation: delayFadeInAnimation 0.5s ease-in-out;
			text-shadow: ${svgDTO.getColor().getTextShadowColor()};
        }
        .portfolio {
			fill : ${svgDTO.getColor().getSubColor()};
            font : 100 12px Noto Sans KR;
			animation: delayFadeInAnimation 0.8s ease-in-out;
			text-shadow: ${svgDTO.getColor().getTextShadowColor()};
        }
		 
        .article {
			fill : ${svgDTO.getColor().getTextColor()};
            font : 400 13px Noto Sans KR;
			animation: delayFadeInAnimation 1.5s ease-in-out;
			text-shadow: ${svgDTO.getColor().getTextShadowColor()};
        }
		 
		.watched{
			animation: delayFadeInAnimation 1.1s ease-in-out;
		}
		 
        .watchedText {
			fill : ${svgDTO.getColor().getTextColor()};
            font : 400 11px  Noto Sans KR;
			animation: delayFadeInAnimation 1.1s ease-in-out;
			text-shadow: ${svgDTO.getColor().getTextShadowColor()};
		}
		 
		.userImage{
			fill : url('#userProfileImage');
			animation: delayFadeInAnimation 0.3s ease-in-out;
		}
		
		.arrow{
			animation: delayFadeInAnimation 1.8s ease-in-out; 
		}
		 
		.arrowInner{
			animation: arrowAnimation 1.5s ease-in-out 1.8s infinite;
		}
		 
		]]> 
	</style>
	 <a href="${svgDTO.getPortfolioUrl()}" title="click to portfolio" target="_blank">
    <g filter="url(#shadow)">
        <rect x="4" y="4" width="${svgDTO.getRectWidth()}" height="${svgDTO.getRectHeight()}" rx="5" fill="${svgDTO.getColor().getMainColor()}"/>
    </g>
	 
    <text class="userName" x="70" y="42"> ${svgDTO.getName()} </text>
	<text class ="portfolio" x="70" y="60"> portfolio </text>
	 
    <g transform="translate(0, ${svgDTO.getArrowY()})" class="arrow">	
		<g class = "arrowInner">
			<path class="arrow"
				  d="M314 190C313.448 190 313 190.448 313 191C313 191.552 313.448 192 314 192V190ZM329.707 191.707C330.098 191.317 330.098 190.683 329.707 190.293L323.343 183.929C322.953 183.538 322.319 183.538 321.929 183.929C321.538 184.319 321.538 184.953 321.929 185.343L327.586 191L321.929 196.657C321.538 197.047 321.538 197.681 321.929 198.071C322.319 198.462 322.953 198.462 323.343 198.071L329.707 191.707ZM314 192H329V190H314V192Z"
				  fill="${svgDTO.getColor().getTextColor()}" />
		</g>
	</g>
	 
    <circle class="userImage" cx="36.5" cy="36.5" r="22.5" />
        
    <g transform="translate(70,80)" class="article">
		${svgDTO.getArticle()}
	</g>
	
	<g class="watched" transform="translate(292, 22)" clip-path="url(#watchedClip)">
		<rect width="20" height="20" rx="10" fill="${svgDTO.getColor().getMainColor()}"/>
		<path d="M18 20C18 24.4183 14.4183 28 10 28C5.58172 28 2 24.4183 2 20C2 15.5817 5.58172 12 10 12C14.4183 12 18 15.5817 18 20Z" fill="${svgDTO.getColor().getTextColor()}"/>
		<circle cx="10" cy="7" r="3" fill="${svgDTO.getColor().getTextColor()}"/>
	</g>
		 
	<text text-anchor="middle" class="watchedText" x="322" y="38">${svgDTO.getWatchedNum()}</text>
	 
    <defs>
        <filter id="shadow" x="0" y="0" width="${svgDTO.getViewBoxWidth()}" height="${svgDTO.getViewBoxHeight()}" filterUnits="userSpaceOnUse"
                color-interpolation-filters="sRGB">
            <feFlood flood-opacity="0" result="BackgroundImageFix" />
            <feColorMatrix in="SourceAlpha" type="matrix" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0"
                result="hardAlpha" />
            <feOffset />
            <feGaussianBlur stdDeviation="2" />
            <feComposite in2="hardAlpha" operator="out" />
            <feColorMatrix type="matrix" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0" />
            <feBlend mode="normal" in2="BackgroundImageFix" result="effect1_dropShadow_493:494" />
            <feBlend mode="normal" in="SourceGraphic" in2="effect1_dropShadow_493:494" result="shape" />
        </filter>
			
        <pattern id="userProfileImage" patternContentUnits="objectBoundingBox" width="1" height="1">
			<image width="1" height="1" href="data:image/jpeg;base64,${svgDTO.getBase64EncodedImage()}"/>
        </pattern>
	
		<clipPath id="watchedClip">
			<rect width="20" height="20" rx="10" fill="${svgDTO.getColor().getMainColor()}"/>
		</clipPath>
    </defs>
	 </a>
</svg>

    
    