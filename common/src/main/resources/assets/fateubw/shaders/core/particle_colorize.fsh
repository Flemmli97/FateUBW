#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

// https://gist.github.com/983/e170a24ae8eba2cd174f
vec3 rgb2hsv(vec3 c)
{
  vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
  vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
  vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

  float d = q.x - min(q.w, q.y);
  float e = 1.0e-10;
  return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c)
{
  vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
  vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
  return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

// Luminance according to ITU-R BT.709-6
float luminance(vec3 color) {
  return 0.2126 * color.r + 0.7152 * color.g + 0.0722 * color.b;
}

float hsl_value(float n1, float n2, float hue)
{
  if (hue > 6.0)
    hue -= 6.0;
  else if (hue < 0.0)
    hue += 6.0;

  if (hue < 1.0)
    return n1 + (n2 - n1) * hue;
  else if (hue < 3.0)
    return n2;
  else if (hue < 4.0)
    return n1 + (n2 - n1) * (4.0 - hue);
  else
    return n1;
}

vec3 hsl_lin_linear_rgb(vec3 hsl) {
  if (hsl[1] == 0) {
    return vec3(hsl[2]);
  } else {
    float m2;
    if (hsl[2] <= 0.5)
      m2 = hsl[2] * (1.0 + hsl[1]);
    else
      m2 = hsl[2] + hsl[1] - hsl[2] * hsl[1];
    float m1 = 2.0 * hsl[2] - m2;

    return vec3(hsl_value(m1, m2, hsl[0] * 6.0 + 2.0),
        hsl_value(m1, m2, hsl[0] * 6.0),
        hsl_value(m1, m2, hsl[0] * 6.0 - 2.0));
  }
}

// Implementing https://github.com/GNOME/gimp/blob/master/app/operations/gimpoperationcolorize.c
vec4 colorize(vec4 tex, vec4 color) {
  float lum = luminance(tex.rgb);
  vec3 color_hsv = rgb2hsv(color.rgb);
  color_hsv[2] = lum;
  vec3 rgb_out = hsl_lin_linear_rgb(color_hsv);
  tex.rgb = rgb_out;
  return tex;
}

void main() {
  vec4 colorTex = texture(Sampler0, texCoord0);
  vec4 colorIn = vertexColor * ColorModulator;
  vec4 color = colorize(colorTex, colorIn);
  color.a *= colorIn.a;
  if (color.a < 0.1) {
    discard;
  }
  fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
