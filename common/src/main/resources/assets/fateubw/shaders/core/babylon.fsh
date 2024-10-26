#version 150

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 position;
in float tickTime;

out vec4 fragColor;

#define PI 3.14159

// The MIT License
// Copyright © 2013 Inigo Quilez
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// https://www.youtube.com/c/InigoQuilez
// https://iquilezles.org/

vec2 hash(vec2 p)
{
    p = vec2(dot(p, vec2(127.1, 311.7)),
    dot(p, vec2(269.5, 183.3)));
    return fract(sin(p)*18.5453);
}

vec2 voronoi(in vec2 x, float time)
{
    vec2 n = floor(x);
    vec2 f = fract(x);
    vec3 m = vec3(8.0);
    for (int j=-1; j<=1; j++)
    for (int i=-1; i<=1; i++)
    {
        vec2  g = vec2(float(i), float(j));
        vec2  o = hash(n + g);
        vec2  r = g - f + (0.5+0.7*sin(time+6.2831*o));
        float d = dot(r, r);
        if (d<m.x)
        m = vec3(d, o);
    }
    return vec2(sqrt(m.x), m.y+m.z);
}
//==========================================

vec2 swirl(float radiusIn, float angleIn, vec2 uvIn, vec2 center){
    angleIn = angleIn * PI;
    center = center == vec2(0.) ? vec2(.5) : center;
    uvIn = uvIn - center;

    float len = length(uvIn);
    float angle = atan(uvIn.y, uvIn.x) + angleIn * smoothstep(radiusIn, 0., len);
    float radius = length(uvIn);
    return vec2(radius * cos(angle), radius * sin(angle)) + center;
}

float portalSwirl(float density, float time, float radius, float angle, vec2 uvIn, vec2 center) {
    vec2 uv = swirl(radius, angle, uvIn, center);
    return voronoi(uv * density, time).x;
}

float intensity(vec3 color) {
    float r = min(color.r, 1.);
    float g = min(color.g, 1.);
    float b = min(color.b, 1.);
    // Value
    float v = max(r, max(g, b));
    float min = min(r, min(g, b));
    // Reverse saturation
    float sR;
    if(v == min) {
        sR = 1;
    } else {
        sR = 1 - (v - min) / v;
    }
    return v * 0.5 + sR * 0.5;
}

void main() {
    // Configs
    float waveStrength = 0.1;
    float frequency = 35.0;
    float waveSpeed = 3.0;
    float colorStrength = 40.0;
    float density = 1.;
    float swirlSpeed = 5.5;
    float swirlRadius = 0.8;
    float angle = 5.;
    float speed = 150.;
    float scaledTime = tickTime * speed;
    //Setup
    vec2 uv = texCoord0;
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(uv, center);

    float ringD = dist * 1.5;
    float timeMult = scaledTime * waveSpeed;
    float multiplier = (ringD < 1.0) ? ((ringD-1.0)*(ringD-1.0)) : 0.0;
    float rings = (sin(frequency*ringD-timeMult)+1.5) * waveStrength * multiplier * max(0., (0.8 - ringD));

    float swirl = max(0., portalSwirl(density, scaledTime * swirlSpeed, swirlRadius, angle, uv, center)) * 2.;
    swirl += pow(1.-swirl, 2.) * 0.3;

    vec4 color = vertexColor * colorStrength * rings * swirl;
    if (color.a <= 0.3) {
        color.a -= pow(color.a, 1.5) * 0.5;
    }
    if (dist > 0.8) {
        color *= max(0., 1. - pow(dist, 3.));
    }
    fragColor = color;
    if (fragColor.a <= 0.2) {
        discard;
    }
}
