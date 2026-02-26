import { NextRequest, NextResponse } from "next/server";

export async function POST(request: NextRequest){
    try{
    const body = await request.json();

    const response = await fetch('http://localhost:8088/orchestrator/processTransaction', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            //TODO: Authorization...
        },
        body: JSON.stringify(body),
    });
    
    const result = await response.json();

    if (!response.ok) {
      return new Response(result, { status: response.status });
    }
    

    return NextResponse.json(result, { status: response.status });
    
    } catch(error){
        return NextResponse.json({ sucess: false, error: (error as Error).message }, { status: 500 });
    }
}