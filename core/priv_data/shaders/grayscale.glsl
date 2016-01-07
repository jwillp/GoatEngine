void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
	vec2 xy = fragCoord.xy / iResolution.xy;//Condensing this into one line
    vec4 texColor = texture2D(iChannel0,xy);//Get the pixel at xy from iChannel0
    vec3 colrgb = vec3(texColor.r,texColor.g,texColor.b);
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
    fragColor =  vec4(gray, gray, gray, 1.0);
    
}