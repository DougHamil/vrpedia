(ns vrpedia.shaders.msdf
  (:require ["three" :as three]))


(def vertex-shader "
     attribute vec2 uv;
     attribute vec4 position;
     uniform mat4 projectionMatrix;
     uniform mat4 modelViewMatrix;
     varying vec2 vUv;
     void main() {
      vUv = uv;
      gl_Position = projectionMatrix * modelViewMatrix * position;
     }
  ")

(def fragment-shader "
      #ifdef GL_OES_standard_derivatives
      #extension GL_OES_standard_derivatives : enable
      #endif
      precision highp float;
      uniform float opacity;
      uniform vec3 color;
      uniform sampler2D map;
      varying vec2 vUv;

      float median(float r, float g, float b) {
        return max(min(r, g), min(max(r, g), b));
      }

      void main() {
        vec3 sample = 1.0 - texture2D(map, vUv).rgb;
        float sigDist = median(sample.r, sample.g, sample.b) - 0.5;
        float alpha = clamp(sigDist/fwidth(sigDist) + 0.5, 0.0, 1.0);
        gl_FragColor = vec4(color.xyz, alpha * opacity);
        if (gl_FragColor.a < 0.0001) discard;
      }
  ")

(defn create-shader [opacity color map]
  (clj->js
   {:uniforms {:opacity {:type "f" :value opacity}
               :map {:type "t" :value map}
               :color {:type "c" :value color}}
    :vertexShader vertex-shader
    :transparent true
    :opacity opacity
    :side three/DoubleSide
    :fragmentShader fragment-shader}))


