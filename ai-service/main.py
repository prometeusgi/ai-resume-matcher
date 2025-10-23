from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="AI Resume Matcher Service")

class MatchRequest(BaseModel):
    resume: str
    job_description: str

class MatchResponse(BaseModel):
    match_score: float
    skills_matched: list[str]
    skills_missing: list[str]
    summary: str

@app.post("/match", response_model=MatchResponse)
def match_texts(req: MatchRequest):
    return MatchResponse(
        match_score=87.5,
        skills_matched=["Java", "Spring Boot", "AWS"],
        skills_missing=["Kafka"],
        summary="Strong match. Missing Kafka experience."
    )
