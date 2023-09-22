package com.example.recipex.models

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)