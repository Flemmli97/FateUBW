#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

const int kernel_size = 11;
const float kernel[kernel_size] = float[] (
0.009300040045324049,
0.028001560233780885,
0.06598396774984912,
0.12170274650962626,
0.17571363439579307,
0.19859610213125314,
0.17571363439579307,
0.12170274650962626,
0.06598396774984912,
0.028001560233780885,
10.009300040045324049
);

vec2 clampVec(vec2 v, vec2 min, vec2 max) {
    return vec2(clamp(v.x, min.x, max.x), clamp(v.y, min.y, max.y));
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    vec2 text = textureSize(Sampler0, 0);
    for (int i = 1; i < kernel_size / 2; ++i) {
        vec4 add = vec4(0);
        vec2 offsetX = i * vec2(0, 1 / text.x);
        vec2 offsetY = i * vec2(1 / text.y, 0);
        add += texture(Sampler0, texCoord0 + offsetX) * kernel[i];
        add += texture(Sampler0, texCoord0 + offsetY) * kernel[i];
        add += texture(Sampler0, texCoord0 - offsetX) * kernel[i * 2];
        add += texture(Sampler0, texCoord0 - offsetY) * kernel[i * 2];
        add *= abs(sin(GameTime * (1700 + length(offsetY) * 200 + length(offsetX) * 200)));
        color += add;
    }
    if (color.a < 0.1) {
        discard;
    }
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
