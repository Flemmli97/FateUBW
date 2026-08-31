#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;

in float vertexDistance;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * ColorModulator;
    float fade = linear_fog_fade(vertexDistance, FogStart, FogEnd);
    if (color.a < 0.1) {
        discard;
    }
    fragColor = vec4(color.rgb * fade, 1);
}
