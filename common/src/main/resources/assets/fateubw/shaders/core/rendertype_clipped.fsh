#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform vec4 ClippingColor;
uniform float ClippingWidth;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

in float clip_distance;

out vec4 fragColor;

void main() {
    if (clip_distance < 0.0) {
        discard;
    }
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    if (clip_distance < ClippingWidth) {
        color.rgb = mix(color.rgb, ClippingColor.rgb, ClippingColor.a);
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}